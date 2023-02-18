import java.io.*;
import java.util.Scanner;

public class App {
    static float alpha = 0, beta = 0, ro = 0, gama = 0;
    static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("Olá!");
        if (args.length > 0) {//caso o tamanho do argumento do comando escrito na linha de comandos seja maior que 0, o programa entrará automaticamente no modo não interativo
            modoNaoInterativo(args);
        } else {
            modoInterativo();//caso contrário, irá rodar no modo interativo
        }
    }

    public static void modoNaoInterativo(String[] args) throws IOException {
        String ficheiroEntrada = args[0];// o argumento da posição 0, corresponde ao ficheiro que contém os valores dos parâmetros
        String[][] dadosEmString = lerCSV(ficheiroEntrada);
        int N = Integer.parseInt(args[6]);// o tamanho da população em estudo encontra-se na 6ª posição
        int numDias = Integer.parseInt(args[8]);// o número de dias, ocupa a 8ª posição
        float h = Float.parseFloat(args[4]);// o valor do passo de integração, ocupa a 4ª posição
        int metodoEscolhido = Integer.parseInt(args[2]);// o comando que dita qual será o modelo matemático usado, é explicitado na 2ª posição
        float S = N - 1;
        float I = N - S;
        float R = 0;
        int caso = valoresDasVariaveis(dadosEmString);
        if (metodoEscolhido == 1) {//quando o método de euler é escolhido
            String[] resultadosEuler = aplicarMetodoEuler(h, gama, alpha, R, I, beta, S, ro, numDias);
            mostrarResultadosMetodoEscolhido(resultadosEuler, S, I, R, N, metodoEscolhido, h, numDias, dadosEmString[caso][0]);
            gerarGraficos(dadosEmString[caso][0], metodoEscolhido, h, numDias, N);
        } else if (metodoEscolhido == 2) {//quando o métodos RK4 é escolhido
            String[] resultadosRK4 = aplicarMetodoRK4(h, gama, I, alpha, R, ro, beta, S, numDias);
            mostrarResultadosMetodoEscolhido(resultadosRK4, S, I, R, N, metodoEscolhido, h, numDias, dadosEmString[caso][0]);
            gerarGraficos(dadosEmString[caso][0], metodoEscolhido, h, numDias, N);
        }
    }

    public static String[][] lerCSV(String ficheiroDeEntrada) throws FileNotFoundException {
        /**
         * @param ficheiroDeEntrada de onde são lidos os valores dos parâmetros (taxas)
         * Este módulo tem como função a leitura dos dados que estão armazenados dentro do ficheiro
         * return dos dados lidos armazenados num array de strings
         */
        Scanner sc = new Scanner(new File(ficheiroDeEntrada));
        int i = 0;
        sc.nextLine();
        while (sc.hasNextLine()) {
            i++;
            sc.nextLine();
        }
        sc.close();
        Scanner ler1 = new Scanner(new File(ficheiroDeEntrada));
        String[][] dados = new String[i][5];
        ler1.nextLine();
        for (int j = 0; j < i; j++) {
            String aux = ler1.nextLine();
            dados[j] = aux.split(";");
        }
        ler1.close();
        return dados;
    }

    public static void modoInterativo() throws IOException {
        String[] Excell = new String[5];
        lerInputs(Excell);
        String[][] dadosEmString = lerCSV(Excell[0]);
        int N = Integer.parseInt(Excell[2]);
        int numDias = Integer.parseInt(Excell[3]);
        float h = Float.parseFloat(Excell[1]);
        int metodoEscolhido = Integer.parseInt(Excell[4]);
        float S = N - 1;
        float I = N - S;
        float R = 0;
        int caso = valoresDasVariaveis(dadosEmString);
        if (metodoEscolhido == 1) {//quando o método de Euler é escolhido
            String[] resultadosEuler = aplicarMetodoEuler(h, gama, alpha, R, I, beta, S, ro, numDias);
            mostrarResultadosMetodoEscolhido(resultadosEuler, S, I, R, N, metodoEscolhido, h, numDias, dadosEmString[caso][0]);
            gerarGraficos(dadosEmString[caso][0], metodoEscolhido, h, numDias, N);
        } else if (metodoEscolhido == 2) {//quando o método RK4 é escolhido
            String[] resultadosRK4 = aplicarMetodoRK4(h, gama, I, alpha, R, ro, beta, S, numDias);
            mostrarResultadosMetodoEscolhido(resultadosRK4, S, I, R, N, metodoEscolhido, h, numDias, dadosEmString[caso][0]);
            gerarGraficos(dadosEmString[caso][0], metodoEscolhido, h, numDias, N);
        }
    }

    public static String gerarNomeFicheirosOutput(String nomePropagador, int metodoEscolhido, float h, int numDias, int N) {
        /**
         * @variable hSemPontos, transforma o valor decimal de h, numa string e retira o ponto que separa a parte decimal da inteira
         * return nome dos ficheiros de saida, composto pelo nome do caso de estudo, pelo nº do método escolhido(1-Euler ou 2-RK4), pelo valor de h, pelo tamanho da população em estudo e pelo nº dias considerados
         */
        String hSemPontos = String.valueOf(h).replace(".", "");
        return nomePropagador + "m" + metodoEscolhido + "p" + hSemPontos + "t" + N + "d" + numDias + ".csv";
    }

    public static int valoresDasVariaveis(String[][] dadosEmString) {
        /**
         * @param dadosEmString, dados que constam no ficheiro lido (taxas)
         * passagem de todos os dados aramazenados nos arrays de Strings para doubles
         * return dos valores presentes na linha lida (-1, visto que a primeira linha do ficheiro de entrada tem caráter exclusivamente orientativo)
         */
        for (int i = 0; i < dadosEmString.length; i++) {
            int pessoa = i + 1;
            System.out.println(pessoa + " - " + dadosEmString[i][0]);
        }
        System.out.println("Escolha o caso que pretende estudar!");
        int caso = Integer.parseInt(ler.nextLine());
        beta = Float.parseFloat(dadosEmString[caso - 1][1].replace(",", "."));
        gama = Float.parseFloat(dadosEmString[caso - 1][2].replace(",", "."));
        ro = Float.parseFloat(dadosEmString[caso - 1][3].replace(",", "."));
        alpha = Float.parseFloat(dadosEmString[caso - 1][4].replace(",", "."));
        System.out.println("Ficheiros gerados com sucesso!");
        return caso - 1;
    }

    public static void lerInputs(String[] Excell) {
        float h, numDias, N;
        System.out.println("Por favor, introduza o nome do ficheiro de onde pretende que os parâmetros sejam lidos!");
        Excell[0] = ler.nextLine();
        System.out.println("========================================================================================================================================================================================");
        System.out.println("Introduza o valor do passo de integração:");
        System.out.println("O número introduzido terá que estar entre zero e um!");
        Excell[1] = ler.nextLine();
        h = Float.parseFloat(Excell[1]);
        while(h<=0||h>=1){
            System.out.println("Erro! Introduza um valor entre zero e um!");
            Excell[1]=ler.nextLine();
            h = Float.parseFloat(Excell[1]);
        }
        System.out.println("========================================================================================================================================================================================");
            System.out.println("Introduza o tamanho da população em estudo:");
            System.out.println("O número introduzido terá que ser maior que zero!");
            Excell[2] = ler.nextLine();
            N = Float.parseFloat(Excell[2]);
            while(N<=0){
                System.out.println("Erro! Introduza um valor maior que zero!");
                Excell[2]=ler.nextLine();
                N = Float.parseFloat(Excell[2]);
            }
        System.out.println("========================================================================================================================================================================================");
            System.out.println("Introduza o número de dias a considerar:");
            System.out.println("O número introduzido terá que ser maior que zero!");
            Excell[3] = ler.nextLine();
            numDias = Float.parseFloat(Excell[3]);
            while(numDias<=0){
                System.out.println("Erro! Introduza um valor maior que zero!");
                Excell[3]=ler.nextLine();
                numDias = Float.parseFloat(Excell[3]);
            }
        System.out.println("========================================================================================================================================================================================");
        System.out.println("Agora, terá de escolher o método que deseja ultilizar!");
            System.out.println("Se desejar utilizar o método de Euler, digite 1.");
            System.out.println("Se desejar utilizar o método de Runge-Kutta de 4ª ordem, digite 2.");
            Excell[4] = ler.nextLine();
    }

    public static float calcularPopulacao(float S, float I, float R) {
        float Populacao;
        Populacao = S + I + R;
        return Populacao;
    }

    public static float calcularFuncaoS(float beta, float S, float I) {
        float dS;
        dS = -beta * S * I;
        return dS;
    }

    public static float calcularFuncaoI(float ro, float beta, float S, float I, float gama, float alpha, float R) {
        float dI;
        dI = (ro * beta * S * I) - (gama * I) + (alpha * R);
        return dI;
    }

    public static float calcularFuncaoR(float gama, float I, float alpha, float R, float ro, float beta, float S) {
        float dR;
        dR = gama * I - alpha * R + ((1 - ro) * beta * S * I);
        return dR;
    }

    public static String[] aplicarMetodoEuler(float h, float gama, float alpha, float y0R, float y0I, float beta, float y0S, float ro, int numDias) {
        float ynS, ynR, ynI;
        float tamanhoPopulacao;
        float k = 1 / h;
        /**
         @param y0S valor inicial de membros da população suscetiveís
         @param y0I valor inicial de membros da população infetados
         @param y0R valor inicial de membros da população recuperados
         return Resultados Matriz onde os valores referente à aproximação matemática pelo método de Euler serão guardados
         */
        int i = 0;
        String[] resultados = new String[numDias];
        for (int j = 1; j < numDias; j++) {
            do {
                ynS = y0S + (h * calcularFuncaoS(beta, y0S, y0I));
                ynI = y0I + (h * calcularFuncaoI(ro, beta, y0S, y0I, gama, alpha, y0R));
                ynR = y0R + (h * calcularFuncaoR(gama, y0I, alpha, y0R, ro, beta, y0S));
                y0S = ynS;
                y0I = ynI;
                y0R = ynR;
                i = i + 1;
            } while (i < k);
            tamanhoPopulacao = calcularPopulacao(ynS, ynI, ynR);
            String resultado = j + ";" + y0S + ";" + y0I + ";" + y0R + ";" + tamanhoPopulacao + ";";
            resultados[j] = resultado;
            i = 0;
        }
        return resultados;
    }

    public static void mostrarResultadosMetodoEscolhido(String[] resultados, float S, float I, float R, int N, int metodoEscolhido, float h, int numDias, String nomePropagador) throws IOException {
        /**
         * @param resultados, resultados gerados pelos modelos matemáticos
         * @param nomePropagador caso escolhido para estudo (por exemplo: Dina ou Ruca)
         */
        FileWriter escrever = new FileWriter(gerarNomeFicheirosOutput(nomePropagador, metodoEscolhido, h, numDias, N));
        PrintWriter out = new PrintWriter(escrever);
        out.print("dia;S;I;R;N;");
        out.println();
        out.print(0 + ";" + S + ";" + I + ";" + R + ";" + N + ";");
        out.println();
        for (int i = 1; i < numDias; i++) {
            out.print(resultados[i]);
            out.println();
        }
        out.flush();
        out.close();
        escrever.close();
    }

    public static float calcularK1S(float h, float beta, float S, float I) {
        float k1S;
        k1S = h * (-beta * S * I);
        return k1S;
    }

    public static float calcularK1I(float h, float ro, float beta, float S, float I, float gama, float alpha, float R) {
        float k1I;
        k1I = h * (ro * beta * S * I - (gama * I) + (alpha * R));
        return k1I;
    }

    public static float calcularK1R(float h, float gama, float I, float alpha, float R, float ro, float beta, float S) {
        float k1R;
        k1R = h * ((gama * I) - (alpha * R) + ((1 - ro) * beta * S * I));
        return k1R;
    }

    public static float calcularK2S(float h, float gama, float I, float alpha, float R, float ro, float beta, float S) {
        float k2S;
        k2S = h * (-beta * (S + ((calcularK1S(h, beta, S, I)) / 2)) * (I + ((calcularK1I(h, ro, beta, S, I, gama, alpha, R)) / 2)));
        return k2S;
    }

    public static float calcularK2I(float h, float gama, float I, float alpha, float R, float ro, float beta, float S) {
        float k2I;
        k2I = h * (ro * beta * (S + ((calcularK1S(h, beta, S, I)) / 2)) * (I + ((calcularK1I(h, ro, beta, S, I, gama, alpha, R)) / 2)) - (gama * (I + ((calcularK1I(h, ro, beta, S, I, gama, alpha, R)) / 2))) + (alpha * (R + ((calcularK1R(h, gama, I, alpha, R, ro, beta, S)) / 2))));
        return k2I;
    }

    public static float calcularK2R(float h, float gama, float I, float alpha, float R, float ro, float beta, float S) {
        float k2R;
        k2R = h * (gama * (I + ((calcularK1I(h, ro, beta, S, I, gama, alpha, R)) / 2)) - (alpha * ((calcularK1R(h, gama, I, alpha, R, ro, beta, S)) / 2)) + ((1 - ro) * beta * (S + ((calcularK1S(h, beta, S, I)) / 2)) * (I + ((calcularK1I(h, ro, beta, S, I, gama, alpha, R)) / 2))));
        return k2R;
    }

    public static float calcularK3S(float h, float gama, float I, float alpha, float R, float ro, float beta, float S) {
        float k3S;
        k3S = h * (-beta * (S + ((calcularK2S(h, gama, I, alpha, R, ro, beta, S)) / 2)) * (I + ((calcularK2I(h, gama, I, alpha, R, ro, beta, S)) / 2)));
        return k3S;
    }

    public static float calcularK3I(float h, float gama, float I, float alpha, float R, float ro, float beta, float S) {
        float k3I;
        k3I = h * (ro * beta * (S + ((calcularK2S(h, gama, I, alpha, R, ro, beta, S)) / 2)) * (I + ((calcularK2I(h, gama, I, alpha, R, ro, beta, S)) / 2)) - (gama * (I + ((calcularK2I(h, gama, I, alpha, R, ro, beta, S)) / 2))) + (alpha * (R + ((calcularK2R(h, gama, I, alpha, R, ro, beta, S)) / 2))));
        return k3I;
    }

    public static float calcularK3R(float h, float gama, float I, float alpha, float R, float ro, float beta, float S) {
        float k3R;
        k3R = h * ((gama * (I + ((calcularK2I(h, gama, I, alpha, R, ro, beta, S)) / 2))) - (alpha * (R + ((calcularK2R(h, gama, I, alpha, R, ro, beta, S)) / 2))) + ((1 - ro) * beta * (S + ((calcularK2S(h, gama, I, alpha, R, ro, beta, S)) / 2)) * (I + ((calcularK2I(h, gama, I, alpha, R, ro, beta, S)) / 2))));
        return k3R;
    }

    public static float calcularK4S(float h, float gama, float I, float alpha, float R, float ro, float beta, float S) {
        float k4S;
        k4S = h * (-beta * (S + calcularK3S(h, gama, I, alpha, R, ro, beta, S)) * (I + calcularK3I(h, gama, I, alpha, R, ro, beta, S)));
        return k4S;
    }

    public static float calcularK4I(float h, float gama, float I, float alpha, float R, float ro, float beta, float S) {
        float k4I;
        k4I = h * ((ro * beta * (S + calcularK3S(h, gama, I, alpha, R, ro, beta, S)) * (I + calcularK3I(h, gama, I, alpha, R, ro, beta, S))) - (gama * (I + calcularK3I(h, gama, I, alpha, R, ro, beta, S))) + (alpha * (R + calcularK3R(h, gama, I, alpha, R, ro, beta, S))));
        return k4I;
    }

    public static float calcularK4R(float h, float gama, float I, float alpha, float R, float ro, float beta, float S) {
        float k4R;
        k4R = h * ((gama * (I + calcularK3I(h, gama, I, alpha, R, ro, beta, S))) - (alpha * (R + calcularK3R(h, gama, I, alpha, R, ro, beta, S))) + ((1 - ro) * beta * (S + calcularK3S(h, gama, I, alpha, R, ro, beta, S)) * (I + calcularK3I(h, gama, I, alpha, R, ro, beta, S))));
        return k4R;
    }

    public static String[] aplicarMetodoRK4(float h, float gama, float y0I, float alpha, float y0R, float ro, float beta, float y0S, int numDias) {
        float ynS, ynI, ynR;
        float tamanhoPopulacao;
        float k = 1 / h;
        /**
         @param y0S valor inicial de membros da população suscetiveís
         @param y0I valor inicial de membros da população infetados
         @param y0R valor inicial de membros da população recuperados
         return guardarResultadosRK4 Matriz onde os valores referente à apoximação matemática (Runnge-Kutta) serão guardados
         */
        String[] guardarResultadosRK4 = new String[numDias];
        int i = 0;
        for (int j = 1; j < numDias; j++) {
            do {
                ynS = y0S + ((calcularK1S(h, beta, y0S, y0I) + 2 * calcularK2S(h, gama, y0I, alpha, y0R, ro, beta, y0S) + 2 * calcularK3S(h, gama, y0I, alpha, y0R, ro, beta, y0S) + calcularK4S(h, gama, y0I, alpha, y0R, ro, beta, y0S)) / 6);
                ynI = y0I + ((calcularK1I(h, ro, beta, y0S, y0I, gama, alpha, y0R) + 2 * calcularK2I(h, gama, y0I, alpha, y0R, ro, beta, y0S) + 2 * calcularK3I(h, gama, y0I, alpha, y0R, ro, beta, y0S) + calcularK4I(h, gama, y0I, alpha, y0R, ro, beta, y0S)) / 6);
                ynR = y0R + ((calcularK1R(h, gama, y0I, alpha, y0R, ro, beta, y0S) + 2 * calcularK2R(h, gama, y0I, alpha, y0R, ro, beta, y0S) + 2 * calcularK3R(h, gama, y0I, alpha, y0R, ro, beta, y0S) + calcularK4R(h, gama, y0I, alpha, y0R, ro, beta, y0S)) / 6);
                y0S = ynS;
                y0I = ynI;
                y0R = ynR;
                i = i + 1;
            } while (i < k);
            tamanhoPopulacao = calcularPopulacao(ynS, ynI, ynR);
            String resultado = j + ";" + y0S + ";" + y0I + ";" + y0R + ";" + tamanhoPopulacao + ";";
            guardarResultadosRK4[j] = resultado;
            i = 0;
        }
        return guardarResultadosRK4;
    }

    public static void gerarGraficos(String nomePropagador, int metodoEscolhido, float h, int numDias, int N) throws IOException {
        /**
         gerarGraficos método que cria o ficheiro que contém os comandos para o gnuplot que permitem gerar os resultados gráficos
         */
        File ficheiro = new File("ficheiro.gp");
        PrintWriter out = new PrintWriter(ficheiro);
        out.print("set terminal pngcairo\n");
        out.print("set output 'grafico" + nomePropagador + metodoEscolhido+".png'\n");
        out.print("set datafile separator ';'\n");
        if (metodoEscolhido == 1) {
            out.print("set title 'Distribuição da noticia falsa (Euler)'\n");
        } else {
            out.print("set title 'Distribuição da noticia falsa (RK4)'\n");
        }
        out.print("set xlabel 'Número de dias'\n");
        out.print("set ylabel 'Tamanho da População'\n");
        out.print("set xrange [0:30]\n");
        out.print("set yrange [0:1000]\n");
        out.print("set key autotitle columnhead\n");
        out.print("plot '" + gerarNomeFicheirosOutput(nomePropagador, metodoEscolhido, h, numDias, N) + "' using 1:2 w l, '' using 1:3 w l, '' using 1:4 w l");
        out.close();
        Runtime run = Runtime.getRuntime();
        run.exec("gnuplot ficheiro.gp");
    }
}