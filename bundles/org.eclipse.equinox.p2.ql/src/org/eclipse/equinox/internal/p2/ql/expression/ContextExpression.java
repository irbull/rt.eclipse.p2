/*******************************************************************************
 * Copyright (c) 2009 Cloudsmith Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Cloudsmith Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.equinox.internal.p2.ql.expression;

import java.util.Iterator;
import org.eclipse.equinox.internal.p2.ql.*;
import org.eclipse.equinox.p2.ql.*;

/**
 * The context expression is the top expression in context queries. It introduces the
 * variable 'everything' and initialized it with the iterator that represents all
 * available items.
 */
final class ContextExpression<T> extends Unary implements IContextExpression<T> {

	private final Class<T> elementClass;

	ContextExpression(Class<T> elementClass, Expression expression) {
		super(expression);
		this.elementClass = elementClass;
	}

	public void toString(StringBuffer bld) {
		operand.toString(bld);
	}

	public IEvaluationContext createContext(Iterator<T> iterator, Object[] params) {
		IEvaluationContext context = new SingleVariableContext(new ParameterContext(params), Variable.EVERYTHING);
		context.setValue(Variable.EVERYTHING, new Everything<T>(elementClass, iterator, operand.needsRepeatedIterations()));
		return context;
	}

	public IEvaluationContext createContext(Iterator<T> iterator, Object[] params, ITranslationSupport ts) {
		IEvaluationContext context = new MultiVariableContext(new ParameterContext(params), new IExpression[] {Variable.EVERYTHING, Variable.TRANSLATIONS});
		context.setValue(Variable.EVERYTHING, new Everything<T>(elementClass, iterator, operand.needsRepeatedIterations()));
		context.setValue(Variable.TRANSLATIONS, ts);
		return context;
	}

	public Class<T> getElementClass() {
		return elementClass;
	}

	public int getExpressionType() {
		return operand.getExpressionType();
	}

	String getOperator() {
		throw new UnsupportedOperationException();
	}

	int getPriority() {
		return operand.getPriority();
	}

	Expression pipeFrom(Expression expr) {
		return new ContextExpression<T>(elementClass, operand.pipeFrom(expr));
	}

	boolean isBoolean() {
		return operand.isBoolean();
	}

	boolean isCollection() {
		return operand.isCollection();
	}
}