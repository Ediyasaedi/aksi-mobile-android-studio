package com.iceka.aksimobile.models;

public class Soal {

    private String pertanyaan;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String jawaban;
    private String id;

    public Soal() {
    }

    public Soal(String id, String pertanyaan, String option1, String option2, String option3, String option4, String jawaban) {
        this.id = id;
        this.pertanyaan = pertanyaan;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.jawaban = jawaban;
    }

    public String getId() {
        return id;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public String getJawaban() {
        return jawaban;
    }
}
