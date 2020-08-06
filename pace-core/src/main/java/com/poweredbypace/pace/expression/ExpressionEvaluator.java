package com.poweredbypace.pace.expression;

public interface ExpressionEvaluator {

	public abstract <T> T evaluate(ExpressionContext context, String expression,
			Class<T> type);

}