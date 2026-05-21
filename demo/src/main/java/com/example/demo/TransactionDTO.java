package com.example.demo;

import java.time.LocalDateTime;

public class TransactionDTO {

    private String id;
    private Double montant;
    private LocalDateTime date;
    private String type;
    private String emetteurId;
    private String emetteurNom;
    private String destinataireId;
    private String destinataireNom;

    public TransactionDTO(Transaction t) {
        this.id              = t.getId();
        this.montant         = t.getMontant();
        this.date            = t.getDate();
        this.type            = t.getType().name();
        this.emetteurId      = t.getEmetteur()     != null
                                ? t.getEmetteur().getId()
                                : null;
        this.emetteurNom     = t.getEmetteur()     != null
                                ? t.getEmetteur().getPrenom() + " " + t.getEmetteur().getNom()
                                : "System";
        this.destinataireId  = t.getDestinataire() != null
                                ? t.getDestinataire().getId()
                                : null;
        this.destinataireNom = t.getDestinataire() != null
                                ? t.getDestinataire().getPrenom() + " " + t.getDestinataire().getNom()
                                : null;
    }

    public String getId()              { return id; }
    public Double getMontant()         { return montant; }
    public LocalDateTime getDate()     { return date; }
    public String getType()            { return type; }
    public String getEmetteurId()      { return emetteurId; }
    public String getEmetteurNom()     { return emetteurNom; }
    public String getDestinataireId()  { return destinataireId; }
    public String getDestinataireNom() { return destinataireNom; }
}