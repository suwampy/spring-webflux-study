package com.study.reactive;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {
    private ItemRepository itemRepository;
    private CartRepository cartRepository;
    private CartService cartService;

    public HomeController(ItemRepository itemRepository,
                          CartRepository cartRepository,
                          CartService cartService) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
    }

    // 루트URL에서 장바구니를 보여주도록
    Mono<Rendering> home() {
        return Mono.just(Rendering.view("home.html")
        .modelAttribute("items",
                this.itemRepository.findAll())
        .modelAttribute("cart",
                this.cartRepository.findById("My Cart")
        .defaultIfEmpty(new Cart("My Cart")))
        .build());
    }

    // 장바구니에 상품 추가가
   @PostMapping("/add/{id}") // <1>
    Mono<String> addToCart(@PathVariable String id) { // <2>
        return this.cartService.addToCart("My Cart",id)
                .thenReturn("redirect:/");
//        return this.cartRepository.findById("My Cart") //
//                .defaultIfEmpty(new Cart("My Cart")) // <3> 없으면 새로 생성해 반환
//                .flatMap(cart -> cart.getCartItems().stream() // <4> 장바구니에 담겨 있는 상품이 있는지 확인
//                        .filter(cartItem -> cartItem.getItem() // CartItem을 순회하면서 새로 장바구니에 담은 것과 동일한 종류의 상품이 있는지 확인
//                                .getId().equals(id)) //
//                        .findAny() // Optional<CartItem>을 반환한다.
//                        .map(cartItem -> {
//                            cartItem.increment();
//                            return Mono.just(cart);
//                        })
//                        .orElseGet(() -> { // <5> 새로 장바구니에 담은 상품이 담겨있지앟는 새 상품이라면
//                            return this.itemRepository.findById(id) //
//                                    .map(CartItem::new) // CartItem을 장바구니에 추가
//                                    .map(cartItem -> {
//                                        cart.getCartItems().add(cartItem);
//                                        return cart;
//                                    });
//                        }))
//                .flatMap(cart -> this.cartRepository.save(cart)) // <6>
//                .thenReturn("redirect:/"); // <7>
    }

    /**
     * map vs faltMap
     * - map : '이것'을 '저것'으로 바꾸는 함수형 도구
     * - flatMap : '이것'의 스트림을 다른 크기로 된 '저것'의 스트림으로 바꾸는 함수형 도구
     * */
}
