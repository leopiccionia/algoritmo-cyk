/* Leonardo Kazuhiko Kawazoe & Leonardo Piccioni de Almeida */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class GLC{
	static final String VAZIO = "&";
	static ArrayList<String> variaveis = new ArrayList<String>();
	static ArrayList<String> terminais = new ArrayList<String>();
	static ArrayList<Regra> regras = new ArrayList<Regra>();
	static ArrayList<String> cadeias =  new ArrayList<String>();
	
	public static void main(String[] args){
		try{
			obtemGLC(new File("inp-glc.txt")); /* obtem a gramatica livre do contexto a partir do arquivo */
			obtemCadeias(new File("inp-cadeias.txt")); /* obtem as cadeias a ser analisadas a partir do arquivo */
			for(String cadeia : cadeias){
				if(cadeia.equals(VAZIO)){
					/* TRATA CADEIA VAZIA */
				}
				else{
					int max = cadeia.length(); /* tamanho do lado da matriz quadrada */
					Lista[][] tabela = new Lista[max][max]; /* tabela contendo listas ligadas para o valor de cada indice*/
					/* obtendo a base da tabela */
					int i = 0;
					for(int j = 0; j < max; j++){
						String terminal = String.valueOf(cadeia.charAt(2 * j)); /* caracteres sao divididos por espacos */
						for(Regra r : regras)
							if(r.dir.equals(terminal)) tabela[i][j].add(r.esq);
					}
					/* entao o resto da tabela */
					for(i = 1; i < max; i++)
						for(int j = max - i - 1; j >= 0; j--)
							achaRegras(tabela, i, j);
					/* DO THE MAGIC HERE */
				}
			}
			/* CONTINUAR */
		}
		catch(FileNotFoundException e){
			System.out.println("Arquivo nao encontrado.");
		}
	}

	static void obtemGLC(File f) throws FileNotFoundException{
		Scanner sc = new Scanner(f);
		sc.useDelimiter(" "); /* usar espaco em branco como delimitador de caracteres */
		int num_variaveis = sc.nextInt();
		int num_terminais = sc.nextInt();
		int num_regras = sc.nextInt();
		for(int i = 0; i < num_variaveis; i++) variaveis.add(sc.next());
		for(int i = 0; i < num_terminais; i++) terminais.add(sc.next());
		for(int i = 0; i < num_regras; i++){
			String esq = sc.next();
			sc.next(); /* pula o simbolo ">" */
			String dir = sc.next();
			if(!ehTerminal(dir))
				dir += " " + sc.next();
			regras.add(new Regra(esq, dir));
		}
	}
	
	static void obtemCadeias(File f) throws FileNotFoundException{
		Scanner sc = new Scanner(f);
		int num_cadeias = sc.nextInt();
		for(int i = 0; i < num_cadeias; i++) cadeias.add(sc.nextLine());	
	}
	
	static void preencheBase(){
		/* TERMINAR */
	}
	
	static boolean ehTerminal(String dir){
		for(String s: terminais)
			if(dir.equals(s)) return true;
		return false;
	}
	
	static void achaRegras(Lista[][] tabela, int i, int j){
		/* i_d, j_d : i e j descendo na diagonal
		/* i_v: i subindo na vertical */
		for(int i_d = 1, j_d = j - i, i_v = i - 1; i_v > 0; i_d++, j_d++, i_v--){
			for(Regra regra : regras)
				for(No n1 = tabela[i_v][j].inicio; n1 != null; n1 = n1.prox)
					for(No n2 = tabela[i_d][j_d].inicio; n2 != null; n2 = n2.prox)
						if(regra.dir.equals(n1 + " " + n2)) tabela[i][j].add(regra.esq);
		}
		/* VERIFICAR SE FAZ SENTIDO */
	}
	
}

class Regra{
	public String esq; /* variavel do lado esquerdo da regra */
	public String dir; /* variaveis do lado direito da regra*/
	
	public Regra(String e, String d){
		this.esq = e;
		this.dir = e;
	}
}

class Lista{
	No inicio = null;
	
	public void add(String s){
		if(inicio == null)
			inicio = new No(s);
		else if(!contem(s)){
			No aux = inicio;
			while(aux.prox != null) aux = aux.prox;
			aux.prox = new No(s);
		}
	}
	
	boolean contem(String s){
		No aux = inicio;
		if(aux == null) return false;
		do{
			if(aux.valor == s) return true;
			aux = aux.prox;
		} while(aux != null);
		return false;
	}
	
	public Lista(){
		inicio = null;
	}
}

class No{
	String valor;
	No prox;
	
	public No(String s){
		valor = s;
		prox = null;
	}
}

/*
	Tabela eh construida da seguinte maneira:
	(1,1)	(1,2)	(1,3)	(1,4)
			(2,2)	(2,3)	(2,4)
					(3,3)	(3,4)
							(4,4)
*/