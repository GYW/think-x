package com.think.x.app;

import io.smallrye.mutiny.Multi;

/**
 * All rights Reserved, Designed By GYW
 *
 * @Title:
 * @Package:
 * @Description:
 * @author: GYW
 * @date: 2023/4/20 10:09
 * @version: v1.0
 */
public class Main {
    public static void main(String[] args) {
//        Multi.createFrom().items(1, 0, 3, 4, 5)
//                .onItem().transform(i -> 2 / i)
//                .select().first(3)
//                .onFailure().recoverWithItem(throwable -> 500)
//                .subscribe().with(System.out::println);

//        Cancellable cancellable = Multi.createFrom().items(1,2,3)
//                .subscribe().with(
//                        System.out::println,
//                        failure -> System.out.println("Failed with " + failure),
//                        () -> System.out.println("Completed"));
//        Multi<Integer> multi = Multi.createFrom().emitter(em -> {
//            em.emit(1);
//            em.emit(2);
//            em.emit(3);
//            em.complete();
//        });
//        Multi<Long> ticks = Multi.createFrom().ticks().every(Duration.ofMillis(100));

        Multi<Object> sequence = Multi.createFrom().generator(() -> 50, (n, emitter) -> {
            System.out.println("n=" + n);
            int next = n + (n / 2) + 1;
            if (n < 50) {
                emitter.emit(next);
            } else {
                System.out.println("over.");
                emitter.complete();
            }
            return next;
        });
        sequence.subscribe().with(System.out::println);

    }
}
