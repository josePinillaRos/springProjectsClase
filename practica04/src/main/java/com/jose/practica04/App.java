package com.jose.practica04;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.jose.practica04.entidades.Categories;
import com.jose.practica04.entidades.Flags;
import com.jose.practica04.entidades.Jokes;
import com.jose.practica04.entidades.Language;
import com.jose.practica04.entidades.Types;
import com.jose.practica04.utilidades.HibernateUtils;
import com.jose.practica04.utilidades.MenuUtils;

/**
 * class App
 * clase que se encarga de toda la logica e interacción con el usuario
 * 
 * @autor Jose Pinilla
 */
public class App {
	static Scanner sc;
	
	/**
	 * Método que abre la conexión con la base de datos y muestra el menú principal
	 */
	public static void main( String[] args ) {
		sc = new Scanner(System.in);
		HibernateUtils.quitarLog();
		if (!HibernateUtils.abrirConexion()) {
			System.out.println("Error al abrir la conexión");
			return;
		}

		System.out.println("Conexión abierta");

		tratarMenu();

		sc.close();
		HibernateUtils.cerrarConexion();
	}

	/**
	 * Método que muestra el menú principal y llama a los métodos de gestión de jokes,
	 * categories, lenguajes y flags
     */
	public static void tratarMenu() {
		
		while (true) {
			System.out.println("\nMenú principal");
			System.out.println("-----------------------------------------------------------------------------\n");
			System.out.println(MenuUtils.imprimirMenu("Gestión de jokes", "Gestión de categories", "Gestión de lenguajes", "Gestión de flags")); 
			String opcion = sc.nextLine();
			if (opcion.equals("0"))
				break;
			switch (opcion) {
			case "1" -> tratarOpcionGestionJokes();
			case "2" -> tratarOpcionGestionCategories();
			case "3" -> tratarOpcionGestionLenguajes();
			case "4" -> tratarOpcionGestionFlags();
			default -> System.out.println("Opción no válida");
			}
		}
	}
	
	// ======================Métodos de gestión de jokes===============================
	
	/**
	 * Método que muestra el menú de gestión de jokes y llama a los métodos de
	 * consultas, inserción, modificación y borrado
	 */
	public static void tratarOpcionGestionJokes() {
		
	    while (true) {
	    	System.out.println("\nMenú de gestión de jokes");
	    	System.out.println("-----------------------------------------------------------------------------\n");
	        System.out.println(MenuUtils.imprimirMenu("Consultas", "Insertar joke", "Modificar joke", "Borrar joke"));
	        String subOpcion = sc.nextLine();
	        if (subOpcion.equals("0"))
	            break;
	        switch (subOpcion) {
	            case "1" -> consultasJokes();
	            case "2" -> insertarJoke();
	            case "3" -> modificarJoke();
	            case "4" -> borrarJoke();
	            default -> System.out.println("Opción no válida");
	        }
	    }
	}

	/**
	 * Método que muestra el menú de consultas de jokes y llama a los métodos de
	 * búsqueda de todos los jokes, búsqueda de jokes por texto y búsqueda de jokes
	 * sin flags
	 */
	private static void consultasJokes() {
		
		while (true) {
			System.out.println("\nMenú de consultas de jokes");
			System.out.println("-----------------------------------------------------------------------------\n");
			System.out.println(MenuUtils.imprimirMenu("Buscar todos los jokes","Buscar joke por texto", "Buscar jokes sin flags"));
			String opcion = sc.nextLine();
			if (opcion.equals("0"))
				break;
			switch (opcion) {
			case "1" -> obtenerTodosLosJokes();
			case "2" -> buscarJokePorTexto();
			case "3" -> buscarJokesSinFlags();
			default -> System.out.println("Opción no válida");
			}
		}
	}

	/**
	 * Método que busca todos los jokes en la base de datos
	 * Los muestra por pantalla
	 */
	private static void obtenerTodosLosJokes() {
		List<Jokes> jokes = HibernateUtils.getAll(Jokes.class);
		if (jokes.isEmpty()) {
			System.out.println("No se ha encontrado ningún chiste");
			return;
		}
		for (Jokes joke : jokes) {
			System.out.println(joke);
		}
		System.out.println("Total de chistes: " + jokes.size());
	}

	/**
	 * Método que busca todos los jokes sin flags
	 */
	private static void buscarJokesSinFlags() {

		List<Jokes> jokes = HibernateUtils.getQuery(
				"FROM Jokes j WHERE j.flagses IS EMPTY", Jokes.class
				);
		if (jokes.isEmpty()) {
			System.out.println("No se ha encontrado ningún chiste sin flags");
			return;
		}
		for (Jokes joke : jokes) {
			System.out.println(joke);
		}
	}

	/**
	 * Método que busca un joke por un texto que introduce el usuario
	 */
	private static void buscarJokePorTexto() {
		
		System.out.print("Introduzca el texto a buscar: ");
		String texto = sc.nextLine().replaceAll("'", "''").toLowerCase();
		List<Jokes> jokes = HibernateUtils.getQuery (
				"FROM Jokes WHERE LOWER(text1) LIKE '%" + texto + "%' OR LOWER(text2) LIKE '%" + texto + "%'", Jokes.class
				);
		if (jokes.isEmpty()) {
			System.out.println("No se ha encontrado ningún chiste con ese texto");
			return;
		}
		for (Jokes joke : jokes) {
			System.out.println(joke);
		}
	}
	
	/**
	 * Método que inserta un joke en la base de datos
	 */
	private static void insertarJoke() {
		Categories categoriaElegida = elegirCategoria();
		Types typeElegido = elegirType();
		Language languajeElegido = elegirLenguaje();
		String[] partes = elegriTexto(typeElegido);
		String texto1 = partes[0];
		String texto2 = partes[1];
		Set<Flags> flags = elegirFlags();
		
		Jokes nuevoJoke = new Jokes();
	    nuevoJoke.setCategories(categoriaElegida); 
	    nuevoJoke.setTypes(typeElegido);                 
	    nuevoJoke.setLanguage(languajeElegido);         
	    nuevoJoke.setText1(texto1);
	    nuevoJoke.setText2(texto2);
	    nuevoJoke.setFlagses(flags);  
		
		if (HibernateUtils.persistAll(nuevoJoke)) {
			System.out.println("Joke guardado correctamente.");
		} else {
			System.out.println("Error al guardar el joke.");
		}
		
	}

	/**
	 * Se le da al usuario una lista de categorias y elige una
	 * @return la categoria elegida
	 */
	private static Categories elegirCategoria() {
		 System.out.println("Categorías disponibles:");

		    List<Categories> categorias = HibernateUtils.getAll(Categories.class);
		    categorias.forEach(System.out::println);

		    Categories categoriaSeleccionada = null;
		    boolean esValido = false;

		    while (!esValido) {
		        System.out.print("Seleccione una categoría por su id: ");
		        String eleccionUsuario = sc.nextLine();

		        try {
		            int idSeleccionado = Integer.parseInt(eleccionUsuario);

		            // Buscar la categoría seleccionada
		            categoriaSeleccionada = HibernateUtils.getId(Categories.class, idSeleccionado);

		            if (categoriaSeleccionada != null) {
		                esValido = true;
		            } else {
		                System.out.println("Categoría no existe. Introduzca un id válido.");
		            }
		        } catch (NumberFormatException e) {
		            System.out.println("Entrada no válida. Por favor, introduzca un número.");
		        }
		    }

		    System.out.println("Categoría seleccionada: " + categoriaSeleccionada);
		    return categoriaSeleccionada;
	}
	
	/**
	 * Se le da al usuario una lista de tipos y elige uno
	 * @return el tipo elegido
	 */
	private static Types elegirType() {
		System.out.println("Tipos disponibles:");

	    // Obtener todos los tipos y mostrarlos
	    List<Types> tipos = HibernateUtils.getAll(Types.class);
	    tipos.forEach(System.out::println);

	    Types tipoSeleccionado = null;
	    boolean esValido = false;

	    while (!esValido) {
	        System.out.print("Seleccione un tipo por su id: ");
	        String eleccionUsuario = sc.nextLine();

	        try {
	            int idSeleccionado = Integer.parseInt(eleccionUsuario);

	            tipoSeleccionado = HibernateUtils.getId(Types.class, idSeleccionado);

	            if (tipoSeleccionado != null) {
	                esValido = true;
	            } else {
	                System.out.println("Tipo no existe. Introduzca un id válido.");
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Entrada no válida. Por favor, introduzca un número.");
	        }
	    }

	    System.out.println("Tipo seleccionado: " + tipoSeleccionado);
	    return tipoSeleccionado;
	}

	/**
	 * Se recoge un texto que introduce el usuario
	 * @param typeElegido tipo de joke
	 * @return un array con el texto 1 y el texto 2
	 */
	private static String[] elegriTexto(Types typeElegido) {
		String t1 = "";
		String t2 = "";
		
		if (typeElegido.getId() == 1) {
			System.out.print("Introduzca el texto: ");
			t1 = sc.nextLine();
			t2 = "";
			return new String[] {t1, t2};		
		} else {
			System.out.print("Introduzca el texto 1: ");
			t1 = sc.nextLine();
			System.out.print("Introduzca el texto 2: ");
			t2 = sc.nextLine();
			return new String[] {t1, t2};
		}
	}
	
	/**
	 * Se le da al usuario una lista de lenguajes y elige uno
	 * @return el lenguaje elegido
	 */
	private static Language elegirLenguaje() {
		System.out.println("Lenguajes disponibles:");

	    List<Language> lenguajes = HibernateUtils.getAll(Language.class);
	    lenguajes.forEach(System.out::println);

	    Language lenguajeSeleccionado = null;
	    boolean esValido = false;

	    while (!esValido) {
	        System.out.print("Seleccione un lenguaje por su id: ");
	        String eleccionUsuario = sc.nextLine();

	        try {
	            int idSeleccionado = Integer.parseInt(eleccionUsuario);

	            lenguajeSeleccionado = HibernateUtils.getId(Language.class, idSeleccionado);

	            if (lenguajeSeleccionado != null) {
	                esValido = true;
	            } else {
	                System.out.println("Lenguaje no existe. Introduzca un id válido.");
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Entrada no válida. Por favor, introduzca un número.");
	        }
	    }

	    System.out.println("Lenguaje seleccionado: " + lenguajeSeleccionado);
	    return lenguajeSeleccionado;
	}
	
	/**
	 * Se le da al usuario una lista de flags y elige uno o varios
	 * @return los flags elegidos
	 */
	private static Set<Flags> elegirFlags() {
		System.out.println("Flags disponibles:");

	    List<Flags> flagsDisponibles = HibernateUtils.getAll(Flags.class);
	    flagsDisponibles.forEach(System.out::println);

	    Set<Flags> flagsSeleccionados = new HashSet<>();
	    boolean continuar = true;

	    while (continuar) {
	        System.out.print("Seleccione un flag por su id (o escriba '0' para finalizar): ");
	        String eleccionUsuario = sc.nextLine();

	        try {
	            int idSeleccionado = Integer.parseInt(eleccionUsuario);

	            if (idSeleccionado == 0) {
	                continuar = false; 
	            } else {

	                Flags flagSeleccionado = HibernateUtils.getId(Flags.class, idSeleccionado);

	                if (flagSeleccionado != null) {
	                    if (!flagsSeleccionados.contains(flagSeleccionado)) {
	                        flagsSeleccionados.add(flagSeleccionado);
	                        System.out.println("Flag añadido: " + flagSeleccionado);
	                    } else {
	                        System.out.println("El flag ya ha sido seleccionado.");
	                    }
	                } else {
	                    System.out.println("Flag no existe. Introduzca un id válido.");
	                }
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Entrada no válida. Por favor, introduzca un número.");
	        }
	    }

	    System.out.println("Flags seleccionados: " + flagsSeleccionados);
	    return flagsSeleccionados;
	}
	
	/**
	 * Metodo sobreescrito con parametro de entrada. Elige flags de una lista de flags disponibles
	 * @param flagsDisponibles
	 * @return los flags elegidos
	 */
	private static Set<Flags> elegirFlags(Set<Flags> flagsDisponibles) {
		System.out.println("Flags disponibles:");

	    flagsDisponibles.forEach(System.out::println);

	    Set<Flags> flagsSeleccionados = new HashSet<>();
	    boolean continuar = true;

	    while (continuar) {
	        System.out.print("Seleccione un flag por su id (o escriba '0' para finalizar): ");
	        String eleccionUsuario = sc.nextLine();

	        try {
	            int idSeleccionado = Integer.parseInt(eleccionUsuario);

	            if (idSeleccionado == 0) {
	                continuar = false; 
	            } else {
	                // Buscar el flag por su id
	                Flags flagSeleccionado = HibernateUtils.getId(Flags.class, idSeleccionado);

	                if (flagSeleccionado != null) {
	                    if (!flagsSeleccionados.contains(flagSeleccionado)) {
	                        flagsSeleccionados.add(flagSeleccionado);
	                        System.out.println("Flag seleccionado: " + flagSeleccionado);
	                    } else {
	                        System.out.println("El flag ya ha sido seleccionado.");
	                    }
	                } else {
	                    System.out.println("Flag no existe. Introduzca un id válido.");
	                }
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Entrada no válida. Por favor, introduzca un número.");
	        }
	    }

	    System.out.println("Flags seleccionados: " + flagsSeleccionados);
	    return flagsSeleccionados;
	}

	/**
	 * Método que modifica un joke
	 * Se le pide al usuario que introduzca el id del joke a modificar
	 * Se le pide al usuario los nuevos valores para modificar el joke
	 */
	private static void modificarJoke() {
	    HibernateUtils.getAll(Jokes.class).forEach(System.out::println); 

	    Jokes jokeAModificar = null;

	    while (jokeAModificar == null) {
	        System.out.print("Introduzca el id del joke a modificar: ");
	        String eleccionUsuario = sc.nextLine();

	        try {
	            int id = Integer.parseInt(eleccionUsuario);

	            jokeAModificar = HibernateUtils.getId(Jokes.class, id);

	            if (jokeAModificar == null) {
	                System.out.println("No se ha encontrado ningún joke con ese ID. Por favor, introduzca un ID válido.");
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Entrada no válida. Por favor, introduzca un número.");
	        }
	    }

	    // Pedir al usuario los nuevos valores para modificar el joke
	    jokeAModificar = preguntasModificarJoke(jokeAModificar);
		
	    boolean resultado = HibernateUtils.merge(jokeAModificar) != null;
	    if (resultado) {
	        System.out.println("Joke actualizado correctamente: " + jokeAModificar);
	    } else {
	        System.out.println("Error al actualizar el joke.");
	    }
	}
	
	/**
	 * Método que muestra las opciones para modificar un joke
	 * pregunta al usuario si desea modificar la categoría, el tipo, el texto, el lenguaje y los flags
	 * 
	 * @param jokeAModificar
	 * @return el joke modificado
	 */
	private static Jokes preguntasModificarJoke (Jokes jokeAModificar) {
		System.out.println("Categoría actual: " + jokeAModificar.getCategories());
		if (leerConfirmacionSN("¿Desea modificar la categoría? (S/N): ")) {
			Categories categoriaElegida = elegirCategoria();
			jokeAModificar.setCategories(categoriaElegida);
		}
		
		System.out.println("Tipo actual: " + jokeAModificar.getTypes());
        if (leerConfirmacionSN("¿Desea modificar el tipo? (S/N): ")) {
			Types typeElegido = elegirType();
			jokeAModificar.setTypes(typeElegido);
			
			System.out.println("Texto actual: " + jokeAModificar.getText1() + " - " + jokeAModificar.getText2());
			String[] partes = elegriTexto(jokeAModificar.getTypes());
			String texto1 = partes[0];
			String texto2 = partes[1];
			jokeAModificar.setText1(texto1);
			jokeAModificar.setText2(texto2);
        } else {
        	System.out.println("Texto actual: " + jokeAModificar.getText1() + " - " + jokeAModificar.getText2());
    		if (leerConfirmacionSN("¿Desea modificar el texto? (S/N): ")) {
    			String[] partes = elegriTexto(jokeAModificar.getTypes());
    			String texto1 = partes[0];
    			String texto2 = partes[1];
    			jokeAModificar.setText1(texto1);
    			jokeAModificar.setText2(texto2);
    		}
        }
	    
        System.out.println("Lenguaje actual: " + jokeAModificar.getLanguage());
        if (leerConfirmacionSN("¿Desea modificar el lenguaje? (S/N): ")) {
            Language languajeElegido = elegirLenguaje();
            jokeAModificar.setLanguage(languajeElegido);
        }
 
		System.out.println("Flags actuales: " + jokeAModificar.getFlagses());
		
		if (leerConfirmacionSN("¿Desea modificar los flags? (S/N): ")) {
			
			while (true) {
		    	System.out.println("\nMenú de gestión de modificación de flags");
		    	System.out.println("-----------------------------------------------------------------------------\n");
		        System.out.println(MenuUtils.imprimirMenu("Añadir flags", "Eliminar flags"));
		        String subOpcion = sc.nextLine();
		        if (subOpcion.equals("0"))
		            break;
		        switch (subOpcion) {
		            case "1" -> jokeAModificar = introducirFlagsJokesModificados(jokeAModificar);
		            case "2" -> jokeAModificar = quitarFlags(jokeAModificar);
		            default -> System.out.println("Opción no válida");
		        }
		    }
		}
		
		return jokeAModificar;
	}
	
	/**
	 * Metodo para añadir flags a un joke modificado
	 * @param jokeAModificar 
	 * @return joke modificado
	 */
	private static Jokes introducirFlagsJokesModificados(Jokes jokeAModificar) {
		Set<Flags> flags = elegirFlags();
		jokeAModificar.addFlagses(flags);
		return jokeAModificar;
	}
		
	/**
	 * Metodo para quitar flags a un joke modificado
	 * @param jokeAModificar
	 * @return joke modificado
	 */
	private static Jokes quitarFlags(Jokes jokeAModificar) {
		Set<Flags> flagsActuales = jokeAModificar.getFlagses();

		if (flagsActuales.isEmpty()) {
			System.out.println("El joke no tiene flags asociados.");
			return jokeAModificar;
		}
		Set<Flags> flagsAQuitar = elegirFlags(flagsActuales);
		jokeAModificar.getFlagses().removeAll(flagsAQuitar);
		return jokeAModificar;
	}
	
	/**
	 * Método que borra un joke Se le pide al usuario que introduzca el id del joke
	 * a borrar Se le pide confirmación al usuario para borrar el joke
	 */
	private static void borrarJoke() {
	    List<Jokes> jokes = HibernateUtils.getAll(Jokes.class);
	    if (jokes.isEmpty()) {
	        System.out.println("No hay jokes disponibles para borrar.");
	        return;
	    }
	    jokes.forEach(System.out::println);
	    
	    Jokes jokeABorrar = null;
	    while (jokeABorrar == null) {
	        System.out.print("\nIntroduzca el id del joke a borrar: ");
	        String entrada = sc.nextLine();

	        try {
	            int id = Integer.parseInt(entrada);

	            jokeABorrar = HibernateUtils.getId(Jokes.class, id);
	            if (jokeABorrar == null) {
	                System.out.println("No existe ningún joke con ese ID. Inténtelo de nuevo.");
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Entrada no válida. Por favor, introduzca un número.");
	        }
	    }

	    // Verificar si el joke tiene relaciones M:M con flags
	    Set<Flags> flagsAsociados = jokeABorrar.getFlagses();
	    
	    if (flagsAsociados != null && !flagsAsociados.isEmpty()) {
	        // Preguntar confirmación si hay relaciones
	        System.out.println("El joke con ID " + jokeABorrar.getId() + 
	                " tiene " + flagsAsociados.size() + " flag(s) asociado(s).");
	        flagsAsociados.forEach(System.out::println);
	        
	        if (!leerConfirmacionSN("¿Desea realmente borrarlo? (S/N): ")) {
	        	System.out.println("Operación cancelada. No se ha borrado el joke.");
	            return;
	        }
	    }

	    // Borrar el joke y sus relaciones M:M
	    boolean resultado = HibernateUtils.remove(jokeABorrar);
	    if (resultado) {
	        System.out.println("Joke borrado correctamente (ID: " + jokeABorrar.getId() + ").");
	    } else {
	        System.out.println("Ha ocurrido un error al intentar borrar el joke.");
	    }
	}
	
	// ===========================Métodos de gestión de categories==================================
	
	/**
	 * Método que muestra el menú de gestión de categories y llama a los métodos de
	 * consultas, inserción, modificación y borrado
     */
	private static void tratarOpcionGestionCategories() {
		 while (true) {
	    	System.out.println("\nMenú de gestión de categories");
	    	System.out.println("-----------------------------------------------------------------------------\n");
	        System.out.println(MenuUtils.imprimirMenu("Consultas", "Insertar categoria", "Modificar categoria", "Borrar categoría"));
	        String subOpcion = sc.nextLine();
	        if (subOpcion.equals("0"))
	            break;
	        switch (subOpcion) {
	            case "1" -> consultasCategorias();
	            case "2" -> insertarCategoria();
	            case "3" -> modificarCategoria();
	            case "4" -> borrarCategoria();
	            default -> System.out.println("Opción no válida");
	        }
	    }
	}

	/**
	 * Método que muestra el menú de consultas de categories y llama a los métodos de
	 * búsqueda de todas las categories, búsqueda de categories por texto y búsqueda de
	 * la category más repetida
	 */
	private static void consultasCategorias() {
		while (true) {
			System.out.println("\nMenú de consultas de categorías");
			System.out.println("-----------------------------------------------------------------------------\n");
			System.out.println(MenuUtils.imprimirMenu("Buscar todas las categorías","Buscar categoría por texto","Buscar categoría más repetida"));
			String opcion = sc.nextLine();
			if (opcion.equals("0"))
				break;
			switch (opcion) {
			case "1" -> buscarTodasLasCategoriasNativeQuery();
			case "2" -> buscarCategoriaPorTextoNativeQuery();
			case "3" -> buscarCategoriaMasRepetida();
			default -> System.out.println("Opción no válida");
			}
		}
	}

	/**
	 * Método que busca todas las categorías en la base de datos
	 * Utiliza una consulta nativa
	 */
	private static void buscarTodasLasCategoriasNativeQuery() {
	    List<Categories> resultados = HibernateUtils.nativeQuery(
	            "SELECT * FROM categories", 
	            Categories.class,
	            null
	    );

	    if (resultados.isEmpty()) {
	        System.out.println("No hay categorías en la base de datos.");
	        return;
	    }

	    for (Categories resultado : resultados) {
	        System.out.println(resultado);
	    }
	}
	
	/**
	 * Método que busca una categoría por un texto que introduce el usuario
	 * Utiliza una consulta nativa
	 */
	private static void buscarCategoriaPorTextoNativeQuery() {
	    HashMap<String, Object> parametros = new HashMap<String, Object>();
	    System.out.println("Introduzca el texto a buscar: ");
	    String texto = sc.nextLine().replaceAll("'", "''").toLowerCase();
	    parametros.put("category", "%" + texto + "%"); 

	    List<Categories> resultados = HibernateUtils.nativeQuery(
	            "SELECT * FROM categories WHERE category ILIKE :category", // ILIKE para no diferenciar mayúsculas y minúsculas
	            Categories.class,
	            parametros);

	    if (resultados.isEmpty()) {
	        System.out.println("No hay resultados");
	        return;
	    }

	    for (Categories resultado : resultados) {
	        System.out.println(resultado);
	    }  
	}

	/**
	 * Método que busca la categoría más repetida Utiliza una consulta nativa
	 */
	private static void buscarCategoriaMasRepetida() {
		 // Consulta nativa 
	    String sql = "SELECT c.* " +
	                 "FROM categories c " +
	                 "JOIN jokes j ON c.id = j.category_id";

	    // Ejecutar la consulta nativa y mapear los resultados a la clase Categories
	    List<Categories> categorias = HibernateUtils.nativeQuery(sql, Categories.class, null);

	    if (categorias == null || categorias.isEmpty()) {
	        System.out.println("No se ha encontrado ninguna categoría asociada a jokes.");
	        return;
	    }

	    // Encontrar la categoría más repetida utilizando distinct, count y reduce 
	    // de los métodos de programación funcional vistos en clase.
	    Categories categoriaMasRepetida = categorias.stream()
	            .distinct() 
	            .reduce((c1, c2) -> // Reducir a la categoría más repetida
	                    categorias.stream().filter(c -> c.equals(c1)).count() > 
	                    categorias.stream().filter(c -> c.equals(c2)).count() ? c1 : c2)
	            .orElse(null);

	    if (categoriaMasRepetida == null) {
	        System.out.println("No se ha podido determinar la categoría más repetida.");
	        return;
	    }

	    // Contar cuántas veces aparece la categoría más repetida
	    long cantidad = categorias.stream()
	            .filter(c -> c.equals(categoriaMasRepetida))
	            .count();

	    System.out.println("Categoría más repetida: " + categoriaMasRepetida +
	            " - Cantidad: " + cantidad);
	}

	/**
	 * Método que inserta una categoría en la base de datos
     */
	private static void insertarCategoria() {
		System.out.print("Introduzca el nombre de la categoría: ");
		String nombre = sc.nextLine();
		Categories nuevaCategoria = new Categories();
		nuevaCategoria.setCategory(nombre);
		if (HibernateUtils.persistAll(nuevaCategoria)) {
			System.out.println("Categoría guardada correctamente.");
		} else {
			System.out.println("Error al guardar la categoría.");
		}
	}

	/**
	 * Método que modifica una categoría
	 * Se le pide al usuario que introduzca el id de la categoría a modificar
	 * Se le pide al usuario el nuevo nombre para modificar la categoría
	 */
	private static void modificarCategoria() {
		HibernateUtils.getAll(Categories.class).forEach(System.out::println);

		Categories categoriaAModificar = null;

		while (categoriaAModificar == null) {
			System.out.print("Introduzca el id de la categoría a modificar: ");
			String eleccionUsuario = sc.nextLine();

			try {
				int id = Integer.parseInt(eleccionUsuario);

				categoriaAModificar = HibernateUtils.getId(Categories.class, id);

				if (categoriaAModificar == null) {
					System.out.println(
							"No se ha encontrado ninguna categoría con ese ID. Por favor, introduzca un ID válido.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Entrada no válida. Por favor, introduzca un número.");
			}
		}

		System.out.println("Nombre actual: " + categoriaAModificar.getCategory());
		if (leerConfirmacionSN("¿Desea modificar el nombre? (S/N): ")) {
			System.out.print("Introduzca el nuevo nombre de la categoría: ");
			String nuevoNombre = sc.nextLine();
			categoriaAModificar.setCategory(nuevoNombre);
		} else {
            System.out.println("Nombre actual: " + categoriaAModificar.getCategory());
            System.out.println("No se ha modificado el nombre");
            return;
		}
            	
		boolean resultado = HibernateUtils.merge(categoriaAModificar) != null;
		if (resultado) {
			System.out.println("Categoría actualizada correctamente: " + categoriaAModificar);
		} else {
			System.out.println("Error al actualizar la categoría.");
		}
	}
	
	/**
	 * Método que borra una categoría Se le pide al usuario que introduzca el id de
	 * la categoría a borrar Se le pide confirmación al usuario para borrar la
	 * categoría
	 */
	private static void borrarCategoria() {
		List<Categories> categorias = HibernateUtils.getAll(Categories.class);
		String nativeQuery = 
			"SELECT j.* " +
	        "FROM jokes j " +
	        "WHERE j.category_id = :id";
		
	    // Escoger la categoría a eliminar
	    Categories catABorrar = escogerElemento(categorias, Categories.class);
	    if (catABorrar == null) {
	        return; 
	    }

	    //Comprobar si hay jokes asociados
	    List<Jokes> jokesAsociados = getJokesAsociadosAlElemento(catABorrar.getId(), nativeQuery);

	    //Si hay jokes asociados, gestionarlos
	    if (!jokesAsociados.isEmpty()) {
	        gestionarJokesAsociadosCategoria(catABorrar, jokesAsociados);
	        return; // aquí ya se hizo todo (posible desvinculación + borrado)
	    }

	    //Si no habia jokes asociados, borrar la categoría directamente
	    borrarElementoFinal(catABorrar, catABorrar.getId(), "categoria");
	}

	/**
	 * Método que gestiona los jokes asociados a una categoría
	 * Pregunta al usuario si desea desvincular los jokes de la categoría o borrarlos
	 * @param catABorrar Categoría a borrar
	 * @param jokesAsociados Lista de jokes asociados a la categoría
	 */
	private static void gestionarJokesAsociadosCategoria(Categories catABorrar, List<Jokes> jokesAsociados) {
	    System.out.println("\nLa categoría con ID " + catABorrar.getId() +
	            " está asociada a " + jokesAsociados.size() + " joke(s).");

	    while (true) {
	        System.out.println("\nMenú de acción de borrado de categorías");
	        System.out.println("-----------------------------------------------------------------------------");
	        System.out.println(MenuUtils.imprimirMenu(
	                "Desvincular los chistes (poner la categoría a null) y luego borrar la categoría",
	                "Borrar los chistes asociados y luego borrar la categoría"
	        ));
	        String opcion = sc.nextLine().trim();
	        if (opcion.equals("0")) {
	            System.out.println("Operación cancelada. No se ha borrado la categoría.");
	            return;
	        }

	        switch (opcion) {
	            case "1":
	                // Desvincular la categoría de cada Joke
	                for (Jokes joke : jokesAsociados) {
	                    Jokes jokeManaged = HibernateUtils.getId(Jokes.class, joke.getId());
	                    if (jokeManaged != null) {
	                        // Quitar la categoría
	                        jokeManaged.setCategories(null);
	                        HibernateUtils.merge(jokeManaged);
	                    }
	                }
	                System.out.println("Se ha desvinculado la categoría de " + jokesAsociados.size() + " chistes.");

	                // Preguntar confirmación para borrar la categoría
	                if (!leerConfirmacionSN("¿Desea ahora borrar la categoría (S/N)?: ")) {
	                    System.out.println("Operación cancelada. No se ha borrado la categoría.");
	                    return;
	                }
	                //Si el usuario confirma, se borra la categoría
	                borrarElementoFinal(catABorrar, catABorrar.getId(), "categoria");
	                return;

	            case "2":
	                // Borrar los jokes asociados
	                System.out.println("Va a borrar " + jokesAsociados.size() + " chiste(s) asociado(s) a la categoría.");
	              
	                if (!leerConfirmacionSN("¿Está seguro? (S/N): ")) {
	                    System.out.println("Operación cancelada. No se ha borrado la categoría.");
	                    return;
	                }
	                
	                //Si el usuario confirma, se borran los jokes asociados
	                for (Jokes joke : jokesAsociados) {
	                    boolean resultado = HibernateUtils.remove(joke);
	                    if (resultado) {
	                        System.out.println("Joke borrado correctamente (ID: " + joke.getId() + ").");
	                    } else {
	                        System.out.println("Ha ocurrido un error al intentar borrar el joke con ID: " + joke.getId());
	                    }
	                }
	                System.out.println("Se han borrado " + jokesAsociados.size() + " chiste(s) asociado(s) a la categoría.");

	                // Confirmamos si se borra la categoría
	                if (!leerConfirmacionSN("¿Desea ahora borrar la categoría (S/N)?: ")) {
	                    System.out.println("Operación cancelada. No se ha borrado la categoría.");
	                    return;
	                }
	                
	                //Si el usuario confirma, se borra la categoría
	                borrarElementoFinal(catABorrar, catABorrar.getId(), "categoria");
	                return;

	            default:
	                System.out.println("Opción no válida");
	                break;
	        }
	    }
	}

	
	// ===========================Métodos de gestión de Language==================================
	
	/**
	 * Método que muestra el menú de gestión de lenguajes y llama a los métodos de
	 * consultas, inserción, modificación y borrado
     */
	private static void tratarOpcionGestionLenguajes() {
		while (true) {
        	System.out.println("\nMenú de gestión de lenguajes");
        	System.out.println("-----------------------------------------------------------------------------\n");
            System.out.println(MenuUtils.imprimirMenu("Consultas", "Insertar lenguaje", "Modificar lenguaje", "Borrar lenguaje"));
            String subOpcion = sc.nextLine();
            if (subOpcion.equals("0"))
                break;
            switch (subOpcion) {
                case "1" -> consultasLenguajes();
                case "2" -> insertarLenguaje();
                case "3" -> modificarLenguaje();
                case "4" -> borrarLenguaje();
                default -> System.out.println("Opción no válida");
            }
        }
	}

	/**
	 * Método que muestra el menú de consultas de lenguajes y llama a los métodos de
	 * búsqueda de todos los lenguajes, búsqueda de lenguajes por texto y búsqueda de
	 * lenguajes sin jokes
	 */
	private static void consultasLenguajes() {
		while (true) {
            System.out.println("\nMenú de consultas de lenguajes");
            System.out.println("-----------------------------------------------------------------------------\n");
            System.out.println(MenuUtils.imprimirMenu("Buscar todos los lenguajes","Buscar lenguaje por texto","Buscar lenguaje sin jokes"));
            String opcion = sc.nextLine();
            if (opcion.equals("0"))
                break;
            switch (opcion) {
            case "1" -> buscarTodosLosLenguajes();
            case "2" -> buscarLenguajePorTexto();
            case "3" -> buscarLenguajeSinJokes();
            default -> System.out.println("Opción no válida");
            }
        }
	}
	
	/**
	 * Método que busca todos los lenguajes en la base de datos
	 * Utiliza una Named Query
	 */
	private static void buscarTodosLosLenguajes() {
	    List<Language> resultados = HibernateUtils.namedQuery(
	            "Language.todos",
	            Language.class,
	            null
	    );

	    if (resultados.isEmpty()) {
	        System.out.println("No hay lenguajes disponibles.");
	        return;
	    }

	    for (Language resultado : resultados) {
	        System.out.println(resultado);
	    }
	}

	/**
	 * Método que busca un lenguaje por un texto que introduce el usuario
	 */
	private static void buscarLenguajePorTexto() {
	    System.out.println("Introduzca el texto a buscar: ");
	    String texto = sc.nextLine().replaceAll("'", "''").toLowerCase();

	    // Crear el mapa de parámetros para la Named Query
	    HashMap<String, Object> parametros = new HashMap<>();
	    parametros.put("searchText", "%" + texto + "%");

	    // Llamada a la Named Query
	    List<Language> resultados = HibernateUtils.namedQuery(
	            "Language.buscarPorTexto",
	            Language.class,
	            parametros
	    );

	    if (resultados.isEmpty()) {
	        System.out.println("No se encontraron lenguajes que coincidan con el texto proporcionado.");
	        return;
	    }

	    for (Language resultado : resultados) {
	        System.out.println(resultado);
	    }
	}

	/**
	 * Método que busca lenguajes sin jokes asociados
	 * Utiliza una Named Query
	 */
	private static void buscarLenguajeSinJokes() {
	    // Llamada a la Named Query
	    List<Language> resultados = HibernateUtils.namedQuery(
	            "Language.lenguajesSinJokes",
	            Language.class,
	            null 
	    );

	    if (resultados.isEmpty()) {
	        System.out.println("No hay lenguajes sin chistes asociados.");
	        return;
	    }
	    for (Language resultado : resultados) {
	        System.out.println(resultado);
	    }
    }

	/**
	 * Método que inserta un lenguaje en la base de datos
	 * Se le pide al usuario que introduzca el código y el nombre del lenguaje
	 */
	private static void insertarLenguaje() {
		String code;
		
        do {
            System.out.println("Introduzca el código del lenguaje (2 letras): ");
            code = sc.nextLine().toLowerCase();
            if (!code.matches("[a-z]{2}")) {
                System.out.println("El código tiene que tener 2 letras.");
            }
        } while (!code.matches("[a-z]{2}"));
			
        System.out.print("Introduzca el nombre del lenguaje: ");
        String language = sc.nextLine();
        
        Language nuevoLenguaje = new Language();
        nuevoLenguaje.setCode(code);
        nuevoLenguaje.setLanguage(language);
        
        if (HibernateUtils.persistAll(nuevoLenguaje)) {
            System.out.println("Lenguaje guardado correctamente.");
        } else {
            System.out.println("Error al guardar el lenguaje.");
        }
	}

	/**
	 * Método que modifica un lenguaje
	 * Se le pide al usuario que introduzca el id del lenguaje a modificar
	 * Se le pide al usuario los nuevos valores para modificar el lenguaje
	 */
	private static void modificarLenguaje() {
		HibernateUtils.getAll(Language.class).forEach(System.out::println);

		Language lenguajeAModificar = null;

		while (lenguajeAModificar == null) {
			System.out.print("Introduzca el id del lenguaje a modificar: ");
			String eleccionUsuario = sc.nextLine();

			try {
				int id = Integer.parseInt(eleccionUsuario);

				lenguajeAModificar = HibernateUtils.getId(Language.class, id);

				if (lenguajeAModificar == null) {
					System.out.println(
							"No se ha encontrado ningún lenguaje con ese ID. Por favor, introduzca un ID válido.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Entrada no válida. Por favor, introduzca un número.");
			}
		}

		System.out.println("Codigo actual: " + lenguajeAModificar.getCode());
		if (leerConfirmacionSN("¿Desea modificar el código? (S/N): ")) {
			String nuevoCode;
			
	        do {
	            System.out.println("Introduzca el nuevo código del lenguaje (2 letras): ");
	            nuevoCode = sc.nextLine().toLowerCase();
	            if (!nuevoCode.matches("[a-z]{2}")) {
	                System.out.println("El código tiene que tener 2 letras.");
	            }
	        } while (!nuevoCode.matches("[a-z]{2}"));
			
			lenguajeAModificar.setCode(nuevoCode);
		} 

		System.out.println("Nombre actual: " + lenguajeAModificar.getLanguage());
		if (leerConfirmacionSN("¿Desea modificar el nombre? (S/N): ")) {
			System.out.print("Introduzca el nuevo nombre del lenguaje: ");
			String nuevoLanguage = sc.nextLine();
			lenguajeAModificar.setLanguage(nuevoLanguage);
		} 

		boolean resultado = HibernateUtils.merge(lenguajeAModificar) != null;
		if (resultado) {
			System.out.println("Lenguaje actualizado correctamente: " + lenguajeAModificar);
		} else {
			System.out.println("Error al actualizar el lenguaje.");
		}
	}

	/**
	 * Método que borra un lenguaje
	 * Se le pide al usuario que introduzca el id del lenguaje a borrar
	 * Se comprueba si el lenguaje tiene jokes asociados
	 * Si tiene jokes asociados, se pregunta al usuario si desea desvincularlos o borrarlos
	 */
	private static void borrarLenguaje() {
		List<Language> lenguajes = HibernateUtils.getAll(Language.class);
		
		if (lenguajes.isEmpty()) {
			System.out.println("No hay lenguajes disponibles para borrar.");
			return;
		}
		
		String nativeQuery =
			"SELECT j.* " +
			"FROM jokes j " +
			"WHERE j.language_id = :id";
		
	    Language langABorrar = escogerElemento(lenguajes, Language.class);
	    if (langABorrar == null) {
	        return; 
	    }

	    //Comprobar si hay jokes asociados
	    List<Jokes> jokesAsociados = getJokesAsociadosAlElemento(langABorrar.getId(), nativeQuery);

	    //Si hay jokes asociados, gestionarlos
	    if (!jokesAsociados.isEmpty()) {
	        gestionarJokesAsociadosLanguage(langABorrar, jokesAsociados);
	        return;
	    }

	    //Si no hay jokes asociados, borrar el lenguaje directamente
	    borrarElementoFinal(langABorrar, langABorrar.getId(), "Language");
	}

	/**
	 * Muestra un menú para el usuario, permitiéndole:
	 *  Desvincular los chistes (poner language a null) y luego borrar el language
	 *  Borrar los chistes y luego borrar el language
	 */
	private static void gestionarJokesAsociadosLanguage(Language langABorrar, List<Jokes> jokesAsociados) {
	    System.out.println("\nEl language con ID " + langABorrar.getId() +
	            " está asociado a " + jokesAsociados.size() + " joke(s).");

	    while (true) {
	        System.out.println("\nMenú de acción de borrado de lenguajes");
	        System.out.println("-----------------------------------------------------------------------------");
	        System.out.println(MenuUtils.imprimirMenu(
	                "Desvincular los chistes (poner language a null) y luego borrar el language",
	                "Borrar los chistes asociados y luego borrar el language"
	        ));
	        String opcion = sc.nextLine().trim();
	        if (opcion.equals("0")) {
	            System.out.println("Operación cancelada. No se ha borrado el language.");
	            return;
	        }

	        switch (opcion) {
	            case "1":
	                // Desvincular el language de cada Joke
	                for (Jokes joke : jokesAsociados) {
	                    Jokes jokeManaged = HibernateUtils.getId(Jokes.class, joke.getId());
	                    if (jokeManaged != null) {
	                        jokeManaged.setLanguage(null);  // Quitar el Language
	                        HibernateUtils.merge(jokeManaged);
	                    }
	                }
	                System.out.println("Se ha desvinculado el language de " + jokesAsociados.size() + " chistes.");

	                // Preguntar confirmación para borrar el language
	                boolean deseaBorrarLang = leerConfirmacionSN("¿Desea ahora borrar el language (S/N)?: ");
	                if (!deseaBorrarLang) {
	                    System.out.println("Operación cancelada. No se ha borrado el language.");
	                    return;
	                }

	                // Borrar el language
	                borrarElementoFinal(langABorrar, langABorrar.getId(), "Language");
	                return;

	            case "2":
	                // Borrar los jokes asociados
	                System.out.println("Va a borrar " + jokesAsociados.size() + " chiste(s) asociado(s) al language.");
	                boolean confirmado = leerConfirmacionSN("¿Está seguro? (S/N): ");
	                if (!confirmado) {
	                    System.out.println("Operación cancelada. No se ha borrado el language.");
	                    return;
	                }

	                for (Jokes joke : jokesAsociados) {
	                    boolean resultado = HibernateUtils.remove(joke);
	                    if (resultado) {
	                        System.out.println("Joke borrado correctamente (ID: " + joke.getId() + ").");
	                    } else {
	                        System.out.println("Ha ocurrido un error al intentar borrar el joke con ID: " + joke.getId());
	                    }
	                }
	                System.out.println("Se han borrado " + jokesAsociados.size() + " chiste(s) asociado(s) al language.");

	                // Confirmar si se borra el language
	                if (!leerConfirmacionSN("¿Desea ahora borrar el language (S/N)?: ")) {
	                    System.out.println("Operación cancelada. No se ha borrado el language.");
	                    return;
	                }

	                // Borrar el language
	                borrarElementoFinal(langABorrar, langABorrar.getId(), "Language");
	                return;

	            default:
	                System.out.println("Opción no válida");
	                break;
	        }
	    }
	}
	
	
	// ===========================Métodos de gestión de Flags==================================
	
	/**
	 * Método que muestra el menú de gestión de flags y llama a los métodos de
	 * consultas, inserción, modificación y borrado
	 */
	private static void tratarOpcionGestionFlags() {
		while (true) {
			System.out.println("\nMenú de gestión de flags");
			System.out.println("-----------------------------------------------------------------------------\n");
			System.out.println(MenuUtils.imprimirMenu("Consultas", "Insertar flag", "Modificar flag", "Borrar flag"));
			String subOpcion = sc.nextLine();
			if (subOpcion.equals("0"))
				break;
			switch (subOpcion) {
			case "1" -> consultasFlags();
			case "2" -> insertarFlag();
			case "3" -> modificarFlag();
			case "4" -> borrarFlag();
			default -> System.out.println("Opción no válida");
			}
		}
	}

	/**
	 * Método que muestra el menú de consultas de flags y llama a los métodos de
	 * búsqueda de todos los flags, búsqueda de flags por texto y búsqueda del
	 * flag más repetido
	 */
	private static void consultasFlags() {
		while (true) {
			System.out.println("\nMenú de consultas de flags");
			System.out.println("-----------------------------------------------------------------------------\n");
			System.out.println(MenuUtils.imprimirMenu("Buscar todos los flags", "Buscar flag por texto",
					"Buscar flag más repetido"));
			String opcion = sc.nextLine();
			if (opcion.equals("0"))
				break;
			switch (opcion) {
			case "1" -> mostrarTodosFlags();
			case "2" -> buscarFlagPorTexto();
			case "3" -> buscarFlagMasRepetido();
			default -> System.out.println("Opción no válida");
			}
		}	
	}
	
	/**
	 * Método que muestra todos los flags en la base de datos
	 */
	private static boolean mostrarTodosFlags() {
		List<Flags> flags = obtenerTodosFlags();
		if (flags.isEmpty()) {
			System.out.println("No hay flags disponibles.");
			return false;
		} else {
			flags.forEach(System.out::println);
			return true;
		}
	}
	
	/**
	 * Método que obtiene todos los flags de
	 * la base de datos
	 * @return Lista de flags
	 */
	private static List<Flags> obtenerTodosFlags() {
		return HibernateUtils.getAll(Flags.class);
	}

	/**
	 * Metodo que busca un flag por un texto que introduce el usuario
	 */
	private static void buscarFlagPorTexto() {
		System.out.print("Introduzca el texto a buscar: ");
		String texto = sc.nextLine().replaceAll("'", "''").toLowerCase();
		
		List<Flags> flags = HibernateUtils.getQuery("FROM Flags WHERE LOWER(flag) LIKE '%" + texto + "%'", Flags.class);
		
		if (flags.isEmpty()) {
			System.out.println("No se ha encontrado ningún flag con ese texto");
			return;
		}
		
		for (Flags flag : flags) {
			System.out.println(flag);
		}
	}

	/**
	 * Método que busca el flag más repetido
	 */
	private static void buscarFlagMasRepetido() {
		
        String sql = "SELECT f.* " +
                     "FROM flags f " +
                     "JOIN jokes_flags jf ON f.id = jf.flag_id";

        List<Flags> flags = HibernateUtils.nativeQuery(sql, Flags.class, null);
        
        flags.forEach(System.out::println);

        if (flags == null || flags.isEmpty()) {
            System.out.println("No se ha encontrado ningún flag asociado a jokes.");
            return;
        }

        // Encontrar el flag más repetido utilizando distinct y count y reduce 
        // de los métodos de programación funcional vistos en clase.
        Flags flagMasRepetido = flags.stream()
                .distinct() 
                .reduce((f1, f2) ->
                        flags.stream().filter(f -> f.equals(f1)).count() > 
                        flags.stream().filter(f -> f.equals(f2)).count() ? f1 : f2)
                .orElse(null);

        if (flagMasRepetido == null) {
            System.out.println("No se ha podido determinar el flag más repetido.");
            return;
        }

        // Contar cuántas veces aparece el flag más repetido
        long cantidad = flags.stream()
                .filter(f -> f.equals(flagMasRepetido))
                .count();

        System.out.println("Flag más repetido: " + flagMasRepetido +
                " - Cantidad: " + cantidad);
	}

	/**
	 * Método que inserta un flag en la base de datos
	 */
	private static void insertarFlag() {
		System.out.print("Introduzca el nombre del flag: ");
        String nombre = sc.nextLine();
        
        Flags nuevoFlag = new Flags();
        nuevoFlag.setFlag(nombre);
        
        if (HibernateUtils.persistAll(nuevoFlag)) {
            System.out.println("Flag guardado correctamente.");
        } else {
            System.out.println("Error al guardar el flag.");
        }
	}

	/**
	 * Método que modifica un flag
	 */
	private static void modificarFlag() {
		if(!mostrarTodosFlags()) {
			System.out.println("No hay flags disponibles.");
			return;
		}

		Flags flagAModificar = null;

		while (flagAModificar == null) {
			System.out.print("Introduzca el id del flag a modificar: ");
			String eleccionUsuario = sc.nextLine();
			
			try {
				int id = Integer.parseInt(eleccionUsuario);

				flagAModificar = HibernateUtils.getId(Flags.class, id);

				if (flagAModificar == null) {
					System.out
							.println("No se ha encontrado ningún flag con ese ID. Por favor, introduzca un ID válido.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Entrada no válida. Por favor, introduzca un número.");
			}
		}

		System.out.println("Flag actual: " + flagAModificar.getFlag());
		if(leerConfirmacionSN("¿Desea modificar el flag? (S/N): ")) {
			System.out.print("Introduzca el nuevo nombre del flag: ");
			String nuevoNombre = sc.nextLine();
			flagAModificar.setFlag(nuevoNombre);
	
			boolean resultado = HibernateUtils.merge(flagAModificar) != null;
			if (resultado) {
				System.out.println("Flag actualizado correctamente: " + flagAModificar);
			} else {
				System.out.println("Error al actualizar el flag.");
			}
		}
	}

	/**
	 * Método que borra un flag
	 */
	private static void borrarFlag() {
		List<Flags> flags = obtenerTodosFlags();
		String nativeQuery = 
			"SELECT j.* " +
	        "FROM jokes j " +
	        "JOIN jokes_flags jf ON j.id = jf.joke_id " +
	        "WHERE jf.flag_id = :id";
		
	    Flags flagABorrar = escogerElemento(flags, Flags.class);
	    if (flagABorrar == null) {
	        return; 
	    }
	
	    List<Jokes> jokesAsociados = getJokesAsociadosAlElemento(flagABorrar.getId(), nativeQuery);

	    //Si hay jokes asociados, gestionarlos
	    if (!jokesAsociados.isEmpty()) {
	        gestionarJokesAsociados(flagABorrar, jokesAsociados);
	        return; 
	    }
	
	    //Si no hay jokes asociados, borrar el flag directamente
	    borrarElementoFinal(flagABorrar, flagABorrar.getId(), "flag");
	}
	
	/**
	 * Muestra un menú para el usuario, permitiéndole:
	 *  Desvincular los chistes (y a continuación borrar el flag)
	 *  Borrar los chistes y luego poder seguir el flujo
	 */
	private static void gestionarJokesAsociados(Flags flagABorrar, List<Jokes> jokesAsociados) {
		System.out.println("\nEl flag con ID " + flagABorrar.getId() +
	            " está asociado a " + jokesAsociados.size() + " joke(s).");

	    while (true) {
	        System.out.println("\nMenú de acción de borrado de flags");
	        System.out.println("-----------------------------------------------------------------------------");
	        System.out.println(MenuUtils.imprimirMenu(
	                "Desvincular los chistes (eliminar el flag de sus relaciones) Y borrar el flag",
	                "Borrar los chistes asociados y luego borrar el flag"
	        ));
	        String opcion = sc.nextLine().trim();
	        if (opcion.equals("0")) {
	            System.out.println("Operación cancelada. No se ha borrado el flag.");
	            return;
	        }

	        switch (opcion) {
	            case "1":
	                for (Jokes joke : jokesAsociados) {
	                    Jokes jokeDesvinculado = HibernateUtils.getId(Jokes.class, joke.getId());
	                    if (jokeDesvinculado != null) {
	                        jokeDesvinculado.getFlagses().remove(flagABorrar);
	                        HibernateUtils.merge(jokeDesvinculado);
	                    }
	                }
	                System.out.println("Se ha desvinculado el flag de " + jokesAsociados.size() + " chistes.");

	                if (!leerConfirmacionSN("¿Desea ahora borrar el flag (S/N)?: ")) {
	                    System.out.println("Operación cancelada. No se ha borrado el flag.");
	                    return;
	                }

	                // Borrar el flag
	                borrarElementoFinal(flagABorrar, flagABorrar.getId(), "flag");
	                return;

	            case "2":
	                // Borrar los jokes asociados
	                System.out.println("Va a borrar " + jokesAsociados.size() + " chiste(s) asociado(s) al flag.");
	                
	                if (!leerConfirmacionSN("¿Está seguro? (S/N): ")) {
	                    System.out.println("Operación cancelada. No se ha borrado el flag.");
	                    return;
	                }

	                for (Jokes joke : jokesAsociados) {
	                    boolean resultado = HibernateUtils.remove(joke);
	                    if (resultado) {
	                        System.out.println("Joke borrado correctamente (ID: " + joke.getId() + ").");
	                    } else {
	                        System.out.println("Ha ocurrido un error al intentar borrar el joke con ID: " + joke.getId());
	                    }
	                }
	                System.out.println("Se han borrado " + jokesAsociados.size() + " chiste(s) asociado(s) al flag.");

	                if (!leerConfirmacionSN("¿Desea ahora borrar el flag (S/N)?: ")) {
	                    System.out.println("Operación cancelada. No se ha borrado el flag.");
	                    return;
	                }
	                
	                // Si el usuario confirma, se borra el flag
	                borrarElementoFinal(flagABorrar, flagABorrar.getId(), "flag");
	                return;

	            default:
	                System.out.println("Opción no válida");
	                break;
	        }
	    }
	}
	
	//=================Métodos auxiliares=================
	
	/**
	 * Borra finalmente el elemento (ya sea tras desvincular o si no había jokes).
	 * @param elementoABorrar Elemento a borrar.
	 * @param idElemento ID del elemento a borrar.
	 * @param nombreElemento Nombre del elemento
	 */
	private static <T> void borrarElementoFinal(T elementoABorrar, long idElemento, String nombreElemento) {
		 boolean confirmado = leerConfirmacionSN(
		            "Va a borrar " + nombreElemento + " con ID: " + idElemento + ". ¿Está seguro? (S/N): "
		    );
		    if (!confirmado) {
		        System.out.println("Operación cancelada. No se ha borrado el " + nombreElemento);
		        return;
		    }

		    boolean resultado = HibernateUtils.remove(elementoABorrar);
		    if (resultado) {
		        System.out.println(nombreElemento + " borrado correctamente (ID: " + idElemento + ").");
		    } else {
		        System.out.println("Ha ocurrido un error al intentar borrar el " + nombreElemento);
		    }
	}
	
	/**
	 * Solicita al usuario que confirme con 'S' o 'N'.
	 * Se repite hasta que el usuario introduzca una opción válida.
	 * 
	 * @param mensaje Mensaje que se muestra al usuario antes de leer.
	 * @return true si el usuario elige 'S', false si el usuario elige 'N'.
	 */
	private static boolean leerConfirmacionSN(String mensaje) {
	    while (true) {
	        System.out.print(mensaje);
	        String entrada = sc.nextLine().trim().toLowerCase();

	        if (entrada.equals("s")) {
	            return true;
	        } else if (entrada.equals("n")) {
	            return false;
	        } else {
	            System.out.println("Debe introducir 's' o 'n'. Intente de nuevo.");
	        }
	    }
	}

	/**
	 * Método que permite al usuario escoger un elemento de una lista
	 * @param lista Lista de elementos
	 * @param clase Clase del elemento
	 * @return Elemento escogido
	 */
	private static <T> T escogerElemento(List<T> lista, Class<T> clase) {
	    // Si la lista está vacía, no hay nada que escoger
	    if (lista.isEmpty()) {
	        System.out.println("No hay elementos disponibles para borrar.");
	        return null;
	    }

	    // Mostrar la lista de entidades
	    lista.forEach(System.out::println);

	    T entidadEscogida = null;
	    while (entidadEscogida == null) {
	        System.out.print("\nIntroduzca el id del elemento a borrar: ");
	        String entrada = sc.nextLine();
	        try {
	            int id = Integer.parseInt(entrada);

	            entidadEscogida = HibernateUtils.getId(clase, id);
	            if (entidadEscogida == null) {
	                System.out.println("No existe ningún elemento con ese ID. Inténtelo de nuevo.");
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Entrada no válida. Por favor, introduzca un número.");
	        }
	    }
	    return entidadEscogida;
	}

	/**
	 * Obtiene la lista de Jokes asociados al elemento proporcionado mediante una Native Query.
	 * @param idElementoABorrar ID del elemento a borrar
	 * @param nativeQuery Consulta nativa para obtener los Jokes asociados
	 * @return Lista de Jokes asociados al elemento
	 */
	private static List<Jokes> getJokesAsociadosAlElemento(long idElementoABorrar, String nativeQuery) {
	    HashMap<String, Object> params = new HashMap<>();
	    params.put("id", idElementoABorrar);
	
	    return HibernateUtils.nativeQuery(
	        nativeQuery,
	        Jokes.class,
	        params
	    );
	}
}
