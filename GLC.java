/* Leonardo Kazuhiko Kawazoe & Leonardo Piccioni de Almeida */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class GLC{
	ArrayList<String> variaveis = new ArrayList<String>;
	ArrayList<String> terminais = new ArrayList<String>;
	
	public static void main(String[] args){
		String path = args[1];
		obtemGLC(path + "inp-glc.txt")); /* obtem a gramatica livre do contexto a partir do arquivo */
		obtemCadeias(new File(path + "inp-cadeias.txt")); /* obtem as cadeias a ser analisadas a partir do arquivo */	
	}

	static void obtemGLC(File f){
		Scanner sc = new Scanner(f);
		sc.useDelimiter(" "); /* usar espaco em branco como delimitador de caracteres */
		int num_variaveis = sc.nextInt();
		int num_terminais = sc.nextInt();
		int num_regras = sc.nextInt();
		for(int i = 0; i < num_variaveis; i++) variaveis.add(sc.next());
		for(int i = 0; i < num_terminais; i++) terminais.add(sc.next()); 
		/* CONTINUAR: regras, etc. */
	}
	
	static void obtemCadeias(File f){
		/* CONTINUAR */
		Scanner sc = new Scanner(f);	
	}
}