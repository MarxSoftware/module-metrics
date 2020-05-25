package com.thorstenmarx.webtools.modules.metrics.engine.functions;

import com.thorstenmarx.webtools.api.analytics.Events;
import com.thorstenmarx.webtools.api.analytics.Fields;
import com.thorstenmarx.webtools.api.analytics.query.ShardDocument;
import com.thorstenmarx.webtools.modules.metrics.engine.api.ConsumerFunction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author marx
 */
public class CartFunction implements ConsumerFunction<ShardDocument, Integer> {

	public enum Type {
		COMPLETED,
		ABANDONED,
		ALL
	}

	private final Type type;

	private final Map<String, Cart> carts;

	public CartFunction(Type type) {
		this.type = type;
		carts = new HashMap<>();
	}

	@Override
	public Integer get() {
		if (Type.COMPLETED.equals(type)){
			return (int)carts.values().stream().filter((cart) -> cart.completed).count();
		} else if (Type.ABANDONED.equals(type)) {
			return (int)carts.values().stream().filter((cart) -> !cart.completed).count();
		}
		return carts.size();
	}

	@Override
	public void accept(final ShardDocument document) {
		if (Events.CartItemAdd.value().equalsIgnoreCase(document.document.getString(Fields.Event.value()))) {
			final String cart_id = document.document.getString("c_cart_id");
			getCart(cart_id);
		} else if (Events.CartItemRemove.value().equalsIgnoreCase(document.document.getString(Fields.Event.value()))) {
			final String cart_id = document.document.getString("c_cart_id");
			getCart(cart_id);
		} else if (Events.Order.value().equalsIgnoreCase(document.document.getString(Fields.Event.value()))) {
			final String cart_id = document.document.getString("c_cart_id");
			getCart(cart_id).completed = true;
		}
	}

	private Cart getCart(final String cart_id) {
		if (!carts.containsKey(cart_id)) {
			carts.put(cart_id, new Cart(cart_id));
		}
		return carts.get(cart_id);

	}

	private static class Cart {

		public boolean completed = false;
		public String id;

		public Cart(String id) {
			this.id = id;
		}
		
		
	}
}
