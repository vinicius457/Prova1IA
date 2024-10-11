import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OitoPuzzleVersao1 {

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
        boolean resultado = iddfsImutavel(estadoInicial, profundidadeMaxima);
        long fimTempo = System.currentTimeMillis();

        System.out.println("Resultado: " + resultado);
        System.out.printf("A versão imutável demorou %.6f segundos\n", (fimTempo - inicioTempo) / 1000.0);
    }

    public static boolean iddfsImutavel(int[][] estadoInicial, int profundidadeMaxima) {
        for (int profundidade = 0; profundidade <= profundidadeMaxima; profundidade++) {
            Set<String> visitados = new HashSet<>();
            List<int[][]> caminho = new ArrayList<>();
            caminho.add(estadoInicial);
            if (dfs(estadoInicial, profundidade, visitados, caminho)) {
                System.out.println("Caminho até a solução:");
                for (int[][] estado : caminho) {
                    imprimirEstado(estado);
                }
                return true;
            }
        }
        return false;
    }

    private static boolean dfs(int[][] estado, int profundidade, Set<String> visitados, List<int[][]> caminho) {
        if (ehObjetivo(estado)) {
            imprimirEstado(estado);
            return true;
        }
        if (profundidade == 0) {
            return false;
        }

        String estadoSerializado = serializarEstado(estado);
        if (visitados.contains(estadoSerializado)) {
            return false;
        }
        visitados.add(estadoSerializado);

        for (int[][] sucessor : gerarSucessores(estado)) {
            caminho.add(sucessor);
            if (dfs(sucessor, profundidade - 1, visitados, caminho)) {
                return true;
            }
            caminho.remove(caminho.size() - 1);
        }
        visitados.remove(estadoSerializado);
        return false;
    }

    private static boolean ehObjetivo(int[][] estado) {
        return Arrays.deepEquals(estado, ESTADO_OBJETIVO);
    }

    private static List<int[][]> gerarSucessores(int[][] estado) {
        int[] posZero = encontrarZero(estado);
        int linhaZero = posZero[0];
        int colunaZero = posZero[1];
        List<int[][]> sucessores = new ArrayList<>();

        for (int[] direcao : DIRECOES) {
            int novaLinha = linhaZero + direcao[0];
            int novaColuna = colunaZero + direcao[1];
            if (novaLinha >= 0 && novaLinha < 3 && novaColuna >= 0 && novaColuna < 3) {
                int[][] novoEstado = copiarEstado(estado);
                novoEstado[linhaZero][colunaZero] = novoEstado[novaLinha][novaColuna];
                novoEstado[novaLinha][novaColuna] = 0;
                sucessores.add(novoEstado);
            }
        }
        return sucessores;
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

    private static int[][] copiarEstado(int[][] estado) {
        int[][] novoEstado = new int[estado.length][];
        for (int i = 0; i < estado.length; i++) {
            novoEstado[i] = estado[i].clone();
        }
        return novoEstado;
    }

    private static String serializarEstado(int[][] estado) {
        StringBuilder sb = new StringBuilder();
        for (int[] linha : estado) {
            for (int valor : linha) {
                sb.append(valor).append(",");
            }
        }
        return sb.toString();
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
