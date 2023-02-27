import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Faturamento;

public class FaturamentoDiario {

    public static void main(String[] args) {
        // Ler o arquivo JSON de faturamento diário
        ObjectMapper mapper = new ObjectMapper();
        List<Faturamento> faturamentos = new ArrayList<>();
        try {
            faturamentos = mapper.readValue(new File("faturamento.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Faturamento.class));
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calcular o faturamento médio mensal
        double faturamentoTotal = 0;
        int diasNoMes = 0;
        for (Faturamento f : faturamentos) {
            if(!f.getValor() == 0.0){ //IGNORAR OS DIAS QUE TEVE FATURAMENTO ZERADO
                faturamentoTotal += f.getValor();
                diasNoMes++;
            }
        }
        double faturamentoMedioMensal = faturamentoTotal / diasNoMes * 30; // Aproximação de 30 dias no mês

        // Encontrar o dia com o menor e o maior faturamento em relação à média
        Faturamento menorFaturamento = null;
        Faturamento maiorFaturamento = null;
        for (Faturamento f : faturamentos) {
            if (menorFaturamento == null || f.getValor() < menorFaturamento.getValor()) {
                menorFaturamento = f;
            }
            if (maiorFaturamento == null || f.getValor() > maiorFaturamento.getValor()) {
                maiorFaturamento = f;
            }
        }
        double menorDiferenca = Math.abs(menorFaturamento.getValor() - faturamentoMedioMensal);
        double maiorDiferenca = Math.abs(maiorFaturamento.getValor() - faturamentoMedioMensal);
        for (Faturamento f : faturamentos) {
            double diferenca = Math.abs(f.getValor() - faturamentoMedioMensal);
            if (diferenca < menorDiferenca) {
                menorFaturamento = f;
                menorDiferenca = diferenca;
            }
            if (diferenca > maiorDiferenca) {
                maiorFaturamento = f;
                maiorDiferenca = diferenca;
            }
        }

        // Contar o número de dias com faturamento maior que a média mensal
        int diasAcimaDaMedia = 0;
        for (Faturamento f : faturamentos) {
            if (f.getValor() > faturamentoMedioMensal) {
                diasAcimaDaMedia++;
            }
        }

        // Imprimir os resultados
        System.out.println("Faturamento médio mensal: R$" + faturamentoMedioMensal);
        System.out.println("Dia com menor faturamento em relação à média: " + menorFaturamento.getDia());
        System.out.println("Dia com maior faturamento em relação à média: " + maiorFaturamento.getDia());
        System.out.println("Número de dias com o faturamento maior que amédia mensal: " + diasAcimaDaMedia);
    }
    
}