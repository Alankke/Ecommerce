package com.Globant.E_commerce.Cart;

import java.util.List;

public interface ICartService {
    Cart createCart(Cart cart);
    Cart getCartById(Long id);
    List<Cart> getAllCarts();
    Cart updateCart(Long id, Cart cart);
    boolean deleteCart(Long id);
}
