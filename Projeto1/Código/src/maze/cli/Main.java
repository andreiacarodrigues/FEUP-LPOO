package maze.cli;
import maze.logic.*;

import java.util.Scanner;

public class Main {
	
	static char m2[][] = {{'x','x','x','x','x','x','x','x','x','x'},
			{'x',' ',' ',' ',' ',' ',' ',' ',' ','x'},
			{'x',' ','x','x',' ','x',' ','x',' ','x'},
			{'x','H','x','x',' ','x',' ','x',' ','x'},
			{'x',' ','x','x',' ','x',' ','x',' ','S'},
			{'x',' ',' ',' ',' ',' ',' ','x',' ','x'},
			{'x',' ','x','x',' ','x',' ','x',' ','x'},
			{'x',' ','x','x','D','x',' ','x',' ','x'},
			{'x',' ','x','x','E',' ',' ',' ',' ','x'},
			{'x','x','x','x','x','x','x','x','x','x'}};

	public static void main(String[] args) {

		Scanner s = new Scanner(System.in);
		int numDragoes = 1;
		
		//1 : escolher o modo de jogo
		System.out.println("Escolher modo: \n0 - Facil (dragao parado) \n"
				+ "1 - Medio (dragao mexe aleatoriamente intercalado com adormecimento)\n"
				+ "2 - Dificil (dragao mexe aleatoriamente)");
		int modo = s.nextInt();
		s.nextLine();
		while(modo < 0 || modo > 2){
			System.out.println("Apenas deve digitar 0, 1 ou 2: ");
			modo = s.nextInt();
			s.nextLine();
		}

		// 2 : escolher o tamanho do labirinto
		System.out.println("Escolher o tamanho do labirinto (apenas tamanhos impares): ");
		int tamanho = s.nextInt();
		s.nextLine();
		while(tamanho % 2 == 0 || tamanho < 5 || tamanho > 17){
			System.out.println("Tamanho errado, digite novamente : ");
			tamanho = s.nextInt();
			s.nextLine();
		}

		// 3: criar o jogo
		Game g = new Game(modo, tamanho, numDragoes);
		System.out.println(g.gamePrintMaze());
	
		// 4 : ciclo de jogo
		boolean running = true;
		do{
			System.out.print("Mover heroi(c-cima; b-baixo; d-direita; e-esquerda): ");
			if(s.hasNext()){
				String move = s.nextLine();
				if(move.equals("c") || move.equals("b") || move.equals("d")|| move.equals("e")){
					running = g.gameLogic(move);
					System.out.println(g.gamePrintMaze());
					if(g.getHero().isDead())
						System.out.println("Foste atacado pelo dragão. Morreste :'(");
				}else{
					System.out.println("Comando Inválido. Insira um novo comando.");
				}
			}else
				System.out.println("Comando Inválido. Insira um novo comando.");
		}while(running);

		System.out.println("Fim ! ");

		s.close();
	}

}
