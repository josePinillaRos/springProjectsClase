package com.jose.practica04.utilidades;

/**
 * Clase que contiene métodos para imprimir el menú
 * 
 * @autor Jose Pinilla
 */
public class MenuUtils {

	/**
	 * Imprime el menú con las opciones que le pasemos
	 * 
	 * @param opciones Opciones que se mostrarán en el menú
	 * @return Menú impreso
	 */
	public static String imprimirMenu(String...opciones) {
		StringBuilder sb = new StringBuilder();
        for(int i = 0; i < opciones.length; i++) {
            sb.append((i + 1) + ".-" + opciones[i] + "\n");
        }
        sb.append("0.- Salir\n");
        sb.append("Seleccione una opcion: ");
        return sb.toString();
    }
}
