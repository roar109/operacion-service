package org.pluto.service.operacion.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import org.pluto.service.operacion.model.Expression;

public class OperationHelper {
	private Vector<String> valoresCadenaOriginal = new Vector<String>();
	private List<Character> cadenaOriginalOperadores = null;
	private List<String> cadenaOperadores = null;
	private Vector<String> cadenaPrefix = new Vector<String>();
	private Stack<String> pila = new Stack<String>();

	/**
	 * Descripcion: Metodo que evalua una expresion que se le pasa de parametros
	 * por ejemplo<br/>
	 * 25000*1251.
	 *
	 * @author hector.mendoza <br/>
	 *         Fecha Creacion: 06/01/2012 <br/>
	 *         Fecha Modificacion: <br/>
	 *         Usuario Modificacion: <br/>
	 * @param expresion
	 * @return result
	 **/
	public Expression evaluate(final Expression expresion) {
		valoresCadenaOriginal = new Vector<String>();
		cadenaOriginalOperadores = null;
		cadenaOperadores = null;
		cadenaPrefix = new Vector<String>();
		pila = new Stack<String>();
		Double result = 0.00;

		inicializarOperadores();
		formatearCadenaEntrada(expresion.getExpression());
		calculatePosfix();

		result = evaluarExpresion();

		if (result.isInfinite() || result.isNaN()) {
			expresion.setValid(Boolean.FALSE);
			expresion.setErrorMessage("Is infinite or NaN");
			return expresion;
		}

		final DecimalFormat df = new DecimalFormat("#0.00");

		expresion.setValid(Boolean.TRUE);
		expresion.setResult(df.format(result).toString());

		return expresion;
	}

	/**
	 * ALGORITMO: For each token in turn in the input infix expression: If the
	 * token is an operand, append it to the postfix output. If the token is an
	 * operator A then: While there is an operator B of higher or equal
	 * precidence than A at the top of the stack, pop B off the stack and append
	 * it to the output. Push A onto the stack. If the token is an opening
	 * bracket, then push it onto the stack. If the token is a closing bracket:
	 * Pop operators off the stack and append them to the output, until the
	 * operator at the top of the stack is a opening bracket. Pop the opening
	 * bracket off the stack.
	 *
	 * When all the tokens have been read: While there are still operator tokens
	 * in the stack: Pop the operator on the top of the stack, and append it to
	 * the output.
	 *
	 * ---------- Evaluar expresion postfija-------
	 *
	 * algorithm evaluate(s,n) 02 // s is a postfix string of length n 03 for i
	 * = 1 to n begin 04 if isvar( s(i) ) then push(value of s(i) ) 05 if isop(
	 * s(i) ) then begin 06 x = pop 07 y = pop 08 do y s(i) x and push result
	 * (note the order) 09 end 10 end 11 x = pop 12 return x 13 end.
	 */
	/**
	 * Metodo que convierte un vector en notacion posfija.
	 *
	 */
	private void calculatePosfix() {
		String tmp = "";
		String charAnterior = "";
		int cont = -1;
		try {
			for (final String c : valoresCadenaOriginal) {
				cont++;
				if (!esOperador(c) && !c.equals("(") && !c.equals(")")) {
					cadenaPrefix.add(c);
				} else if (esOperador(c) && !c.equals("(") && !c.equals(")")) {
					if ("-".equals(c)) {
						charAnterior = cont == 0 ? "" : valoresCadenaOriginal.get(cont - 1);
						if (esOperador(charAnterior) || charAnterior.equals("(") || "".equals(charAnterior)) {
							cadenaPrefix.add("0");
						}
					}
					while (!pila.isEmpty() && (evaluar(c) <= evaluar(pila.peek()))) {
						tmp = pila.pop();
						cadenaPrefix.add(tmp);
					}
					pila.push(c);
				} else if (c.equals("(")) {
					pila.push(c);
				} else if (c.equals(")")) {
					while (!pila.isEmpty() && (!pila.peek().equals("("))) {
						tmp = pila.peek();
						if (esOperador(tmp)) {
							cadenaPrefix.add(pila.pop());
						}
					}
					if (!pila.isEmpty() && (pila.peek().equals("("))) {
						pila.pop();
					}
				}
			}
			if (!pila.isEmpty()) {
				while (!pila.isEmpty()) {
					tmp = pila.pop();
					cadenaPrefix.add(tmp);
				}
			}

		} catch (final Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Evaluar los pesos de lo operadores.
	 */
	private static int evaluar(final String a) {
		int eva = 0;
		if (a.equals("+") || a.equals("-")) {
			eva = 1;
		} else if (a.equals("/") || a.equals("*")) {
			eva = 2;
		} else if (a.equals("^")) {
			eva = 3;
		}
		return eva;
	}

	/**
	 * Le da formato a la cadena de entrada, separa la cadena por numeros o
	 * operadores.
	 */
	private void formatearCadenaEntrada(final String cadenaEntrada) {
		String tmp = "";
		boolean ultimoFueOperador = false;
		boolean existeParentesisAbierto = false;

		for (final Character c : cadenaEntrada.toCharArray()) {
			if (!esOperador(c) && ((c != '(') && (c != ')'))) {
				tmp = tmp + c;
				ultimoFueOperador = false;
			} else if (esOperador(c)) {
				if (tmp != "") {
					valoresCadenaOriginal.add(tmp);
				}
				tmp = "";
				if (existeParentesisAbierto) {
					valoresCadenaOriginal.add(")");
					existeParentesisAbierto = false;
				}
				if (ultimoFueOperador) {
					valoresCadenaOriginal.add("(");
					existeParentesisAbierto = true;
				}
				valoresCadenaOriginal.add(c + "");
				ultimoFueOperador = true;
			} else if ((c == '(') || (c == ')')) {
				if (tmp != "") {
					valoresCadenaOriginal.add(tmp);
				}
				tmp = "";
				valoresCadenaOriginal.add(c + "");
				if (existeParentesisAbierto) {
					valoresCadenaOriginal.add(")");
					existeParentesisAbierto = false;
				}
				ultimoFueOperador = false;
			}
		}

		if (tmp != "") {
			valoresCadenaOriginal.add(tmp);
			if (existeParentesisAbierto) {
				valoresCadenaOriginal.add(")");
				existeParentesisAbierto = false;
			}
		}
	}

	private void inicializarOperadores() {
		if (cadenaOriginalOperadores == null) {
			cadenaOriginalOperadores = new ArrayList<Character>();
		} else {
			cadenaOriginalOperadores.clear();
		}

		cadenaOriginalOperadores.add('+');
		cadenaOriginalOperadores.add('-');
		cadenaOriginalOperadores.add('/');
		cadenaOriginalOperadores.add('*');

		if (cadenaOperadores == null) {
			cadenaOperadores = new ArrayList<String>();
		} else {
			cadenaOperadores.clear();
		}

		cadenaOperadores.add("+");
		cadenaOperadores.add("-");
		cadenaOperadores.add("/");
		cadenaOperadores.add("*");
	}

	/**
	 * Revisa si el caracter que se le pasa esta dentro de los operadores
	 * permitidos.
	 */
	private boolean esOperador(final Character o) {
		final boolean esOperando = false;
		for (final Character s : cadenaOriginalOperadores) {
			if (s == o) {
				return true;
			}
		}
		return esOperando;
	}

	/**
	 * Revisa si el string que se le pasa esta dentro de los operadores
	 * permitidos.
	 */
	private boolean esOperador(final String o) {
		final boolean esOperando = false;
		for (final String s : cadenaOperadores) {
			if (o.equals(s)) {
				return true;
			}
		}
		return esOperando;
	}

	/**
	 * Metodo que evalua cada expresion individualmente, por pares de operandos
	 * y un operador a la vez.
	 *
	 */
	private Double evaluarExpresion() {
		final Stack<Double> valores = new Stack<Double>();
		Double x = 0.00;
		Double y = 0.00;
		Double tmp = 0.00;

		try {
			for (final String var : cadenaPrefix) {
				if (!esOperador(var)) {
					valores.push(Double.valueOf(var));
				}
				if (esOperador(var)) {
					x = valores.pop();
					y = valores.pop();
					tmp = hacerOperacion(x, y, var);
					valores.push(tmp);
					tmp = 0.0;
				}
			}
			x = valores.pop();
		} catch (final Exception e) {
			x = 0.0;
		}

		return x;
	}

	/**
	 * Metodo que hace el calculo de la operacion dependiendo el operador que se
	 * le pase como parametro.
	 *
	 */
	private Double hacerOperacion(final Double y, final Double x, final String operador) {
		Double resultado = 0.0;
		try {
			if ("+".equals(operador)) {
				resultado = x + y;
			} else if ("-".equals(operador)) {
				resultado = x - y;
			} else if ("*".equals(operador)) {
				resultado = x * y;
			} else if ("/".equals(operador)) {
				resultado = x / y;
			}
		} catch (final Exception e) {
			resultado = 0.0;
		}

		return resultado;
	}
}
