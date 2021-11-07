package com.study.reactive;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ServerController {
    private final KitchenService kitchen;

    public ServerController(KitchenService kitchen) {
        this.kitchen = kitchen;
    }

    @GetMapping(value = "/server", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Dish> serverDishes() {
        return this.kitchen.getDishes();
    }

    // 요리를 전달하는 함수
    @GetMapping(value= "/served-dishes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Dish> deliverDishes() {
        return this.kitchen.getDishes()
                // 매핑 함수를 사용해서 변환한 후에 반환
                // delivered 값이 true로 설정된 새로운 요리를 만들어냄
                // 함수형 프로그래밍에서는 기존 객체의 상태를 변환하는 대신
                // 변환된 상태를 가진 새 객체를 만들어 사용하는 방식을 선호
                .map(dish -> Dish.deliver(dish));
    }
}
