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

* `src/test/java/...` : tests unitaires et intégration
* `@SpringBootTest` : tests d’intégration
* `@WebMvcTest` : tests de contrôleur

### Rapport de couverture avec JaCoCo

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
