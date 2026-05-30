# Retours et Next Steps — Projet API Stagiaires

## Retours sur le travail livré

### 1. Collection Postman

- Organiser la collection par dossiers logiques (par ressource, par domaine)
- Nommer les requêtes de manière claire et cohérente
- Tous les champs de payload et les URLs doivent être en anglais

### 2. Standardisation des réponses API

Implémenter un wrapper générique Java pour uniformiser toutes les réponses :

```java
public class Wrapper<T> {
    private String message;
    private T data;       // null en cas d'echec
    private String error; // null en cas de succes

    // constructeurs, getters, setters
}
```

Actuellement, le payload succès et le payload échec ont des structures différentes. Ce n'est pas acceptable en production.

### 3. Organisation du code

Structure flat à adopter :

```
demo/
├── controllers/
├── services/
├── repositories/
├── objects/
│   ├── daos/
│   └── dtos/
└── config/
```


## Next Steps — Plan d'apprentissage

### Etape 1 — Design Patterns

Consulter le site de référence : https://refactoring.guru/fr/design-patterns

En priorité, apprendre et être capable d'expliquer ces patterns de base :

| Pattern        | Categorie   |
|----------------|-------------|
| Singleton      | Creation    |
| Builder        | Creation    |
| Factory Method | Creation    |
| Observer       | Comportement |
| Strategy       | Comportement |

### Etape 2 — API REST

**Nommage des ressources — règle absolue :**

- Toujours en anglais, kebab-case
- Représentent une ressource, jamais une action

```
GET    /users
GET    /users/{id}
POST   /users
PUT    /users/{id}
DELETE /users/{id}
```

**Autres points à maîtriser :**

- Verbes HTTP sémantiques : GET, POST, PUT, PATCH, DELETE
- Codes HTTP corrects : 200, 201, 400, 401, 403, 404, 500
- Versioning : /api/v1/...
- Pagination et filtrage sur les endpoints de liste

### Etape 3 — Keycloak

Sequence d'apprentissage :

1. **Concepts** — OAuth 2.0, OpenID Connect (OIDC)
2. **Deploiement local de keycloak avec Docker**
3. **Configuration** — Créer un Realm, un Client, des Roles et des Users depuis la console admin
4. **Integration Spring Boot** — spring-boot-starter-oauth2-resource-server, validation des JWT
5. **Securiser les APIs existantes** — @PreAuthorize, mapping rôles Keycloak vers Spring Security
6. **Tester dans Postman** — Récupérer un token, l'injecter dans les requêtes
