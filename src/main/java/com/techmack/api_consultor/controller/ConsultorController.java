package com.techmack.api_consultor.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConsultorController {

    // responsavel por guardar os contadores, das estatisticas
    private static Map<String, Integer> contadores = new HashMap<>();

    private static List<String> historico = new ArrayList<>();

    // iniciando contadores das estatisticas
    static {
        contadores.put("cep", 0);
        contadores.put("fatos-gatos", 0);
        contadores.put("piada", 0);
    }

    @GetMapping("/")
    public String home() {
        return """

                <h1>Consultor APIS - Spring Boot</h1>
                <h2>EndPoints Disponiveis: </h2>
                <ul>
                    <li><a href=" ">Buscar CEP</a></li>
                    <li><a href=" ">Fatos Gatos</a></li>
                    <li><a href=" ">Piadas</a></li>
                    <li><a href=" ">Alguma Opção...</a></li>
                </ul>
                        """;

    }

    // meotodo resoinsavel reutilizado do consultorAPI original
    private String fazerRequisicao(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection conexao = (HttpsURLConnection) url.openConnection();

        conexao.setRequestMethod("GET");
        conexao.setRequestProperty("User-agent", "Mozilla/5.0");

        BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

        String sLinha;
        StringBuilder resposta = new StringBuilder();

        while ((sLinha = leitor.readLine()) != null) {
            resposta.append(sLinha);
        }
        leitor.close();

        return resposta.toString();

    }

    @GetMapping("/cep/{cep}")
    public String consultarCep(@PathVariable String sCep) {

        try {
            String sUrl = "https://viacep.com.br/ws/" + sCep + "/json/";
            String sJsonResposta = fazerRequisicao(sUrl);

            return "";
        }

        catch (Exception erro) {
            return "Aconteceu algum erro:" + erro.getMessage();
        }

    }

    private String extrairValorJson(String sJson, String sChave) {

        try {

            String sBusca = "\"" + sChave + "\":\"";
            int iInicio = sJson.indexOf(sBusca);

            if (iInicio == -1) {
                sBusca = "\"" + sChave + "\":";
                iInicio = sJson.indexOf(sBusca);
                if (iInicio == -1) {
                    return "Campo não Encontrado";
                }

                iInicio += sBusca.length();
                int iFim = sJson.indexOf("," + iInicio);

                if (iFim == -1) {
                    iFim = sJson.indexOf("}" + iInicio);
                }

                return sJson.substring(iInicio, iFim).trim();
            }

            iInicio += sBusca.length();
            int iFim = sJson.indexOf("\"" + iInicio);
            return sJson.substring(iInicio, iFim).trim();

        } catch (Exception erro) {
            return "Aconteceu algum erro:" + erro.getMessage();
        }

    }

}
