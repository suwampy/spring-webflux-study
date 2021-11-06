package com.study.reactivce.kitchen;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class KitchenService {

    /**
     * 요리 스트림 생성
     * */
    Flux<Dish> getDishes() {
        // Flux.generate()를 사용해 요리를 연속적으로 계속 만들어 제공해줌
        // sink는 무작위로 제공되는 요리를 둘러싸는 Flux의 핸들로써
        // Flux에 포함될 원소를 동적으로 발행할 수 있게 해준다
        return Flux.<Dish> generate(sink -> sink.next(randomDish())) //
                .delayElements(Duration.ofMillis(250));
    }

    /**
     * 요리 무작위 선택
     * */
    private Dish randomDish() {
        return menu.get(picker.nextInt(menu.size()));
    }

    private List<Dish> menu = Arrays.asList( //
            new Dish("Sesame chicken"), //
            new Dish("Lo mein noodles, plain"), //
            new Dish("Sweet & sour beef"));

    private Random picker = new Random();
}
