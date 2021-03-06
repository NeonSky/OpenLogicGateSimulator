package org.cafebabe.model.util;

import java.util.function.Consumer;

/**
 * Associates an owner object to a consumer
 * This is useful since we sometimes want to
 * keep track on who created a certain lambda.
 * @param <T> is the param type that the lambda takes.
 */
class OwnedConsumer<T>  {
    Consumer<T> consumer;
    Object owner;

    OwnedConsumer(Consumer consumer, Object owner) {
        this.consumer = consumer;
        this.owner = owner;
    }
}
