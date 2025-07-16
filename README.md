# Numdev
---

````markdown
# Yoga App  – Tests & Qualité

Ce projet full-stack repose sur :
- Angular (frontend)
- Spring Boot (backend)
- Jest (tests unitaires front-end)
- Cypress (tests end-to-end)
- JUnit + Spring Test (tests back-end)

---

## Tests unitaires front-end (Jest)

### Installation

```bash
npm install
````

> Avec ces dépendances :
>
> * `jest`
> * `jest-preset-angular`
> * `ts-jest`
> * `@types/jest`

### Pour lancer les tests unitaires

```bash
npm run test
```

### Le rapport d'intégration

```bash
 npm run test:integration
```


### Pour mode surveillance (watch)

```bash
npm run test:watch
```

### Le rapport de couverture

```bash
npm run test
```

Un dossier `coverage/jest/` est généré. Ouvrir `index.html` dans ce dossier pour voir les résultats graphiques.

---

## Tests end-to-end (Cypress)

```
npm run e2e:ci
```

Le rapport est disponible à cet endroit :

> front/coverage/index.html
---

## Tests back-end (Spring Boot + JUnit)

### Lancer les tests back-end

```bash
./mvnw test
```

### Structure

- `src/test/java/...` : tests unitaires et intégration
  - `*Test.java` → tests unitaires
  - `*IT.java` → tests d'intégration (Failsafe)
- `@SpringBootTest` → tests d’intégration
- `@WebMvcTest` → tests de contrôleur unitaires

### Rapport de couverture avec JaCoCo

Avec la configuration JaCoCo dans le `pom.xml`, deux rapports sont générés :

-  **Tests unitaires** :
    ```bash
    ./mvnw test
    open target/site/jacoco/index.html
    ```
- **Tests d'intégration** :
    ```bash
    ./mvnw verify
    open target/site/jacoco-integration-test-coverage-report/index.html
    ```

> Le dossier `target/site/jacoco/` contient la couverture **unitaire**.
> 
> Le dossier `target/site/jacoco-integration-test-coverage-report/` contient la couverture **d'intégration**.

Pour tout générer d'un coup (unitaires + intégration + rapports HTML) :
```bash
./mvnw clean verify site
```

Dans `pom.xml`, vérifiez que vous avez :

```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.8</version>
  <executions>
    <execution>
      <goals>
        <goal>prepare-agent</goal>
        <goal>report</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

Puis lancez :

```bash
./mvnw test
open target/site/jacoco/index.html
```
