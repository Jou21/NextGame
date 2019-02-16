
package com.game.next.nextgame.entidades;

public class Jogo {
    private String categoria;
    private String codigoDeBarra;
    private String codigoDeBarra1;
    private String codigoDeBarra10;
    private String codigoDeBarra2;
    private String codigoDeBarra3;
    private String codigoDeBarra4;
    private String codigoDeBarra5;
    private String codigoDeBarra6;
    private String codigoDeBarra7;
    private String codigoDeBarra8;
    private String codigoDeBarra9;
    private String dataLancamento;
    private String descricao;
    private String faixaEtaria;
    private String multiplayer;
    private String nome;
    private String preco;
    private String rating;
    private String sku;
    private String urlImgComplementar1;
    private String urlImgComplementar2;
    private String urlImgComplementar3;
    private String urlImgComplementar4;
    private String urlImgComplementar5;
    private String urlImgJogo;
    private String urlVideo;

    public Jogo(String nome, String preco, String urlImgJogo, String descricao,
            String rating, String categoria, String faixaEtaria, String multiplayer, String dataLancamento,
            String sku, String urlImgComplementar1, String urlImgComplementar2, String urlImgComplementar3,
            String urlImgComplementar4, String urlImgComplementar5, String urlVideo) {
        this.nome = nome;
        this.preco = preco;
        this.urlImgJogo = urlImgJogo;
        this.descricao = descricao;
        this.rating = rating;
        this.categoria = categoria;
        this.faixaEtaria = faixaEtaria;
        this.multiplayer = multiplayer;
        this.dataLancamento = dataLancamento;
        this.sku = sku;
        this.urlImgComplementar1 = urlImgComplementar1;
        this.urlImgComplementar2 = urlImgComplementar2;
        this.urlImgComplementar3 = urlImgComplementar3;
        this.urlImgComplementar4 = urlImgComplementar4;
        this.urlImgComplementar5 = urlImgComplementar5;
        this.urlVideo = urlVideo;
    }
    public Jogo() {
        
    }

    // Getter Methods

    public String getCategoria() {
        return categoria;
    }

    public String getCodigoDeBarra() {
        return codigoDeBarra;
    }

    public String getCodigoDeBarra1() {
        return codigoDeBarra1;
    }

    public String getCodigoDeBarra10() {
        return codigoDeBarra10;
    }

    public String getCodigoDeBarra2() {
        return codigoDeBarra2;
    }

    public String getCodigoDeBarra3() {
        return codigoDeBarra3;
    }

    public String getCodigoDeBarra4() {
        return codigoDeBarra4;
    }

    public String getCodigoDeBarra5() {
        return codigoDeBarra5;
    }

    public String getCodigoDeBarra6() {
        return codigoDeBarra6;
    }

    public String getCodigoDeBarra7() {
        return codigoDeBarra7;
    }

    public String getCodigoDeBarra8() {
        return codigoDeBarra8;
    }

    public String getCodigoDeBarra9() {
        return codigoDeBarra9;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getFaixaEtaria() {
        return faixaEtaria;
    }

    public String getMultiplayer() {
        return multiplayer;
    }

    public String getNome() {
        return nome;
    }

    public String getPreco() {
        return preco;
    }

    public String getRating() {
        return rating;
    }

    public String getSku() {
        return sku;
    }

    public String getUrlImgComplementar1() {
        return urlImgComplementar1;
    }

    public String getUrlImgComplementar2() {
        return urlImgComplementar2;
    }

    public String getUrlImgComplementar3() {
        return urlImgComplementar3;
    }

    public String getUrlImgComplementar4() {
        return urlImgComplementar4;
    }

    public String getUrlImgComplementar5() {
        return urlImgComplementar5;
    }

    public String getUrlImgJogo() {
        return urlImgJogo;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    // Setter Methods

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCodigoDeBarra(String codigoDeBarra) {
        this.codigoDeBarra = codigoDeBarra;
    }

    public void setCodigoDeBarra1(String codigoDeBarra1) {
        this.codigoDeBarra1 = codigoDeBarra1;
    }

    public void setCodigoDeBarra10(String codigoDeBarra10) {
        this.codigoDeBarra10 = codigoDeBarra10;
    }

    public void setCodigoDeBarra2(String codigoDeBarra2) {
        this.codigoDeBarra2 = codigoDeBarra2;
    }

    public void setCodigoDeBarra3(String codigoDeBarra3) {
        this.codigoDeBarra3 = codigoDeBarra3;
    }

    public void setCodigoDeBarra4(String codigoDeBarra4) {
        this.codigoDeBarra4 = codigoDeBarra4;
    }

    public void setCodigoDeBarra5(String codigoDeBarra5) {
        this.codigoDeBarra5 = codigoDeBarra5;
    }

    public void setCodigoDeBarra6(String codigoDeBarra6) {
        this.codigoDeBarra6 = codigoDeBarra6;
    }

    public void setCodigoDeBarra7(String codigoDeBarra7) {
        this.codigoDeBarra7 = codigoDeBarra7;
    }

    public void setCodigoDeBarra8(String codigoDeBarra8) {
        this.codigoDeBarra8 = codigoDeBarra8;
    }

    public void setCodigoDeBarra9(String codigoDeBarra9) {
        this.codigoDeBarra9 = codigoDeBarra9;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setFaixaEtaria(String faixaEtaria) {
        this.faixaEtaria = faixaEtaria;
    }

    public void setMultiplayer(String multiplayer) {
        this.multiplayer = multiplayer;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setUrlImgComplementar1(String urlImgComplementar1) {
        this.urlImgComplementar1 = urlImgComplementar1;
    }

    public void setUrlImgComplementar2(String urlImgComplementar2) {
        this.urlImgComplementar2 = urlImgComplementar2;
    }

    public void setUrlImgComplementar3(String urlImgComplementar3) {
        this.urlImgComplementar3 = urlImgComplementar3;
    }

    public void setUrlImgComplementar4(String urlImgComplementar4) {
        this.urlImgComplementar4 = urlImgComplementar4;
    }

    public void setUrlImgComplementar5(String urlImgComplementar5) {
        this.urlImgComplementar5 = urlImgComplementar5;
    }

    public void setUrlImgJogo(String urlImgJogo) {
        this.urlImgJogo = urlImgJogo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }
}