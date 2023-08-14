package core.services.logicservices;

import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.models.goods.Product;
import com.company.core.models.goods.ProductType;
import com.company.core.models.user.customer.Cart;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.logicservices.ItemService;
import com.company.core.services.logicservices.OrderService;
import com.company.core.services.logicservices.ProductService;
import com.company.core.services.logicservices.StorageService;
import com.company.core.services.logicservices.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;

public class CartServiceImplTest {
    @Mock
    private ItemService itemService;
    @Mock
    private ProductService productService;
    @Mock
    private OrderService orderService;
    @Mock
    private StorageService storageService;
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartService = new CartServiceImpl(itemService, productService, orderService, storageService);
    }

    @Test
    void testAddToCart_whenProductAlreadyInCart() {
        Product product = new Product(1L, "null", "null", ProductType.FOOD, BigDecimal.valueOf(10));

        Mockito.when(storageService.getQuantityPerShop(any(), any())).thenReturn(3);
        Mockito.when(productService.getProduct(any())).thenReturn(product);
        Mockito.when(itemService.createItem(any(), any())).thenReturn(new Item(product, 1));

        Cart cart = new Cart();
        Item existingItem = new Item(product, 2);
        cart.addItem(existingItem);

        assertTrue(cartService.addToCart(cart, 1L, 1, 1L));
        assertEquals(1, cart.getCartItems().size());
        assertEquals(3, cart.getItem(product).getQuantity());
    }

    @Test
    void testAddToCart_whenProductNotInCart() {
        Product product = new Product(1L, "null", "null", ProductType.FOOD, BigDecimal.valueOf(10));

        Mockito.when(storageService.getQuantityPerShop(any(), any())).thenReturn(10);
        Mockito.when(productService.getProduct(any())).thenReturn(product);
        Mockito.when(itemService.createItem(any(), any())).thenReturn(new Item(product, 3));

        Cart cart = new Cart();

        assertTrue(cartService.addToCart(cart, 1L, 3, 1L));
        assertEquals(1, cart.getCartItems().size());
        assertEquals(3, cart.getItem(product).getQuantity());
    }

    @Test
    void testCheckoutCart_whenEmptyCart() {
        Customer customer = new Customer();
        customer.setCart(new Cart());

        assertFalse(cartService.checkoutCart(customer, 1L));
        assertEquals(0, customer.getShoppingCart().getCartItems().size());
    }

    @Test
    void testCheckoutCart_whenSuccessfulOrder() {
        Mockito.when(orderService.createOrder(any(), any())).thenReturn(new Order());
        Mockito.when(orderService.processOrder(any(), any(), any())).thenReturn(true);

        Customer customer = new Customer();
        Cart cart = new Cart();
        customer.setCart(cart);
        Product product = new Product(1L, "null", "null", ProductType.FOOD, BigDecimal.valueOf(10));
        Item item = new Item(product, 5);
        cart.addItem(item);

        assertTrue(cartService.checkoutCart(customer, 1L));
        assertTrue(customer.getShoppingCart().isEmpty());
        Mockito.verify(orderService, Mockito.times(1)).createOrder(cart.getCartItems(), customer);
        Mockito.verify(orderService, Mockito.times(1)).processOrder(any(), ArgumentMatchers.eq(customer), ArgumentMatchers.eq(1L));
    }
}
