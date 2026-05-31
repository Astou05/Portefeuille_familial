// package com.example.demo.objects.dtos;

// import java.time.LocalDateTime;
// import com.example.demo.models.Transaction;

// public class TransactionDTO {

//     private String id;
//     private Double amount;
//     private LocalDateTime date;
//     private String type;
//     private String emetteurId;
//     private String emetteurNom;
//     private String destinataireId;
//     private String destinataireNom;

//     public TransactionDTO(Transaction t) {
//         this.id              = t.getId();
//         this.amount         = t.getAmount();
//         this.date            = t.getDate();
//         this.type            = t.getType().name();
//         this.emetteurId      = t.getEmetteur()     != null
//                                 ? t.getEmetteur().getId()
//                                 : null;
//         this.emetteurNom     = t.getEmetteur()     != null
//                                 ? t.getEmetteur().getFirstName() + " " + t.getEmetteur().getName()
//                                 : "System";
//         this.destinataireId  = t.getDestinataire() != null
//                                 ? t.getDestinataire().getId()
//                                 : null;
//         this.destinataireNom = t.getDestinataire() != null
//                                 ? t.getDestinataire().getFirstName() + " " + t.getDestinataire().getName()
//                                 : null;
//     }

//     public String getId()              { return id; }
//     public Double getAmount()         { return amount; }
//     public LocalDateTime getDate()     { return date; }
//     public String getType()            { return type; }
//     public String getEmetteurId()      { return emetteurId; }
//     public String getEmetteurNom()     { return emetteurNom; }
//     public String getDestinataireId()  { return destinataireId; }
//     public String getDestinataireNom() { return destinataireNom; }
// }


package com.example.demo.objects.dtos;

import java.time.LocalDateTime;

import com.example.demo.objects.daos.Transaction;

public class TransactionDTO {

    private String id;
    private Double amount;
    private LocalDateTime date;
    private String type;
    private String senderId;
    private String senderName;
    private String recipientId;
    private String recipientName;

    public TransactionDTO(Transaction t) {
        this.id            = t.getId();
        this.amount        = t.getAmount();
        this.date          = t.getDate();
        this.type          = t.getType().name();
        this.senderId      = t.getEmetteur()     != null ? t.getEmetteur().getId()     : null;
        this.senderName    = t.getEmetteur()     != null
                             ? t.getEmetteur().getFirstName() + " " + t.getEmetteur().getName()
                             : "System";
        this.recipientId   = t.getDestinataire() != null ? t.getDestinataire().getId() : null;
        this.recipientName = t.getDestinataire() != null
                             ? t.getDestinataire().getFirstName() + " " + t.getDestinataire().getName()
                             : null;
    }

    public String getId()            { return id; }
    public Double getAmount()        { return amount; }
    public LocalDateTime getDate()   { return date; }
    public String getType()          { return type; }
    public String getSenderId()      { return senderId; }
    public String getSenderName()    { return senderName; }
    public String getRecipientId()   { return recipientId; }
    public String getRecipientName() { return recipientName; }
}