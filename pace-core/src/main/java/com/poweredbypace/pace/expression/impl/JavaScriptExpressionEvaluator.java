package com.poweredbypace.pace.expression.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.expression.ExpressionContext;
import com.poweredbypace.pace.expression.ExpressionEvaluator;

@Component
public class JavaScriptExpressionEvaluator implements ExpressionEvaluator {

	private final Log log = LogFactory.getLog(getClass());
	
	public JavaScriptExpressionEvaluator() {
	}
		
	@Override
	public <T> T evaluate(ExpressionContext expressionContext, String expression, Class<T> type) {
		Context context = Context.enter();
		context.setOptimizationLevel(-1);
		context.setLanguageVersion(Context.VERSION_ES6);
		Scriptable scope = context.initStandardObjects();
		
		initContext(expressionContext, scope);
        String script = "var s = " + expression + "; s;";
		try {
			
			Object result = context.evaluateString(scope, script, "script", 1, null);
	        return type.cast( Context.jsToJava(result, type) );
			
		} finally {
			Context.exit();
		}
	}
	
	private void initContext(ExpressionContext context, Scriptable scope) {
		for(String key:context.keySet()) {
			scope.put(key, scope, context.get(key));
		}
		ScriptableObject.putProperty(scope, "log", Context.javaToJS(log, scope));
	}
}
