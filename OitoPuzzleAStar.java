import java.util.*;

public class OitoPuzzleAStar {

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

        long inicioTempo = System.currentTimeMillis();
        boolean resultado = aStar(estadoInicial);
        long fimTempo = System.currentTimeMillis();

        System.out.println("Resultado: " + resultado);
        System.out.printf("O algoritmo A* demorou %.6f segundos\n", (fimTempo - inicioTempo) / 1000.0);
    }

    public static boolean aStar(int[][] estadoInicial) {
        Set<String> visitados = new HashSet<>();
        PriorityQueue<Nodo> fila = new PriorityQueue<>(Comparator.comparingInt(n -> n.custoTotal));

        Nodo nodoInicial = new Nodo(estadoInicial, null, 0, calcularHeuristica(estadoInicial));
        fila.add(nodoInicial);

        while (!fila.isEmpty()) {
            Nodo atual = fila.poll();
            visitados.add(Arrays.deepToString(atual.estado));

            if (ehObjetivo(atual.estado)) {
                imprimirSolucao(atual);
                return true;
            }

            for (int[][] sucessor : gerarSucessores(atual.estado)) {
                String sucessorStr = Arrays.deepToString(sucessor);
                if (!visitados.contains(sucessorStr)) {
                    int g = atual.custoCaminho + 1;
                    int h = calcularHeuristica(sucessor);
                    Nodo novoNodo = new Nodo(sucessor, atual, g, h);
                    fila.add(novoNodo);
                }
            }
        }
        return false;
    }

    private static int calcularHeuristica(int[][] estado) {
        int distancia = 0;
        for (int i = 0; i < estado.length; i++) {
            for (int j = 0; j < estado[i].length; j++) {
                int valor = estado[i][j];
                if (valor != 0) {
                    int objetivoX = valor / 3;
                    int objetivoY = valor % 3;
                    distancia += Math.abs(i - objetivoX) + Math.abs(j - objetivoY);
                }
            }
        }
        return distancia;
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

    private static void imprimirEstado(int[][] estado) {
        for (int[] linha : estado) {
            for (int valor : linha) {
                System.out.print(valor + " ");
            }
            System.out.println();
        }
        System.out.println("----------");
    }

    private static void imprimirSolucao(Nodo nodo) {
        List<int[][]> caminho = new ArrayList<>();
        while (nodo != null) {
            caminho.add(0, nodo.estado);
            nodo = nodo.pai;
        }
        for (int[][] estado : caminho) {
            imprimirEstado(estado);
        }
    }

    static class Nodo {
        int[][] estado;
        Nodo pai;
        int custoCaminho;
        int heuristica;
        int custoTotal;

        Nodo(int[][] estado, Nodo pai, int custoCaminho, int heuristica) {
            this.estado = estado;
            this.pai = pai;
            this.custoCaminho = custoCaminho;
            this.heuristica = heuristica;
            this.custoTotal = custoCaminho + heuristica;
        }
    }
}
