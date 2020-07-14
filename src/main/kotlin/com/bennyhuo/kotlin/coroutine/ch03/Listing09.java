package com.bennyhuo.kotlin.coroutine.ch03;

import org.jetbrains.annotations.NotNull;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class Listing09 {
    public static void main(String[] args) {
        Object result = Listing08Kt.notSuspend(new Continuation<Integer>() {
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NotNull Object o) {
                System.out.println("resumeWith");
            }
        });
        System.out.println(result);
    }
}
