import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class OitoPuzzleVersao2 {

    private static final int[][] ESTADO_OBJETIVO = {
        {0, 1, 2},
        {3, 4, 5},
        {6, 7, 8}
    };

    private static final int[][] DIRECOES = {
        {-1, 0},
        {1, 0},
        {0, -1},
        {0, 1}
    };

    public static void main(String[] args) {
        int[][] estadoInicial = {
            {1, 2, 3},
            {4, 0, 5},
            {7, 8, 6}
        };

        int profundidadeMaxima = 50;

        long inicioTempo = System.currentTimeMillis();
        boolean resultado = iddfsMutavel(estadoInicial, profundidadeMaxima);
        long fimTempo = System.currentTimeMillis();

        System.out.println("Resultado: " + resultado);
        System.out.printf("A versão mutável demorou %.6f segundos\n", (fimTempo - inicioTempo) / 1000.0);
    }

    public static boolean iddfsMutavel(int[][] estadoInicial, int profundidadeMaxima) {
        int[] posZero = encontrarZero(estadoInicial);
        for (int profundidade = 0; profundidade <= profundidadeMaxima; profundidade++) {
            Set<String> visitados = new HashSet<>();
            List<int[][]> caminho = new ArrayList<>();
            caminho.add(copiarEstado(estadoInicial));
            if (dfs(estadoInicial, profundidade, posZero[0], posZero[1], visitados, caminho)) {
                System.out.println("Caminho até a solução:");
                for (int[][] estado : caminho) {
                    imprimirEstado(estado);
                }
                return true;
            }
        }
        return false;
    }

    private static boolean dfs(int[][] estado, int profundidade, int linhaZero, int colunaZero, Set<String> visitados, List<int[][]> caminho) {
        if (ehObjetivo(estado)) {
            return true;
        }
        if (profundidade == 0) {
            return false;
        }

        visitados.add(Arrays.deepToString(estado));

        for (int[] direcao : DIRECOES) {
            int novaLinha = linhaZero + direcao[0];
            int novaColuna = colunaZero + direcao[1];

            if (novaLinha >= 0 && novaLinha < 3 && novaColuna >= 0 && novaColuna < 3) {
                trocar(estado, linhaZero, colunaZero, novaLinha, novaColuna);

                if (!visitados.contains(Arrays.deepToString(estado))) {
                    caminho.add(copiarEstado(estado));

                    if (dfs(estado, profundidade - 1, novaLinha, novaColuna, visitados, caminho)) {
                        return true;
                    }

                    caminho.remove(caminho.size() - 1);
                }

                trocar(estado, linhaZero, colunaZero, novaLinha, novaColuna);
            }
        }
        return false;
    }

    private static boolean ehObjetivo(int[][] estado) {
        return Arrays.deepEquals(estado, ESTADO_OBJETIVO);
    }

    private static int[] encontrarZero(int[][] estado) {
        for (int i = 0; i < estado.length; i++) {
            for (int j = 0; j < estado[i].length; j++) {
                if (estado[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private static void trocar(int[][] estado, int linha1, int coluna1, int linha2, int coluna2) {
        int temp = estado[linha1][coluna1];
        estado[linha1][coluna1] = estado[linha2][coluna2];
        estado[linha2][coluna2] = temp;
    }

    private static int[][] copiarEstado(int[][] estado) {
        int[][] novoEstado = new int[estado.length][];
        for (int i = 0; i < estado.length; i++) {
            novoEstado[i] = estado[i].clone();
        }
        return novoEstado;
    }

    private static void imprimirEstado(int[][] estado) {
        for (int[] linha : estado) {
            for (int valor : linha) {
                System.out.print(valor + " ");
            }
            System.out.println();
        }
        System.out.println("----------");
    }
}
