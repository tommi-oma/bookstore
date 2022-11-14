Bookstore - koulutuksen pohjaprojekti
==============================================================================================


## Projekti

Projekti on Maven-pohjainen EAR-projekti, jonka pohjalle voi rakentaa Java EE harjoituksissa pyydettyjä Java EE piirteitä.

Projektin palvelimena toimii Wildfly, testattu versiolla 17. Tietokantana käytetään tuotannossa käytetään PostgreSQL-palvelinta, testeissä Wildflyn sisäänrakennettua H2-tietokantaa, ja sen muistipohjaista versiota.


## Ennakkovalmistelut

Projekti tarvitsee 
- Java 17 tai uudempikin saattaa toimia
- Maven
- Uusi Wildfly versio, testattu versiolla 26.1.2.Final. Muokkaa pom.xml jos käytät eri versiota eikä meinaa toimia


## Wildflyn käynnistys

1. Navigoi komentorivillä asennushakemistoon
2. Käynnistä standalone-versio:

        Windows: JBOSS_HOME\bin\standalone.bat  (tai .ps1)
        Linux: JBOSS_HOME/bin/standalone.sh

 
## Pohjaprojektin kääntäminen ja levitys

1. Varmista että Wildfly on käynnissä
2. Käännä ja paketoi `.ear`:

        mvn clean package

3. Tuloksena `bookstore-ear` aliprojektiin `target/bookstore-ear.ear`
4. Joudut _ehkä_ myös asentamaan sovelluksen arkkityypit paikallisesti, varsinkin jos paketointi ei onnistu. Eli saat esimerkiksi virheilmoituksen että web-projektin vaatimaa ejb-riippuvuutta ei löydy

        mvn install
        
5. Nyt voit levittää earin. Seuraava komento vie sen käynnissä olevalle Wildfly-palvelimelle 

        mvn wildfly:deploy

6. Koita onnistuuko kirjojen haku: <http://localhost:8080/bookstore-web/api/bookstore>


## Päivitys

Kaikki seuraavat kun Wildfly on käynnissä
1. Käännös ja levitys (clean vapaaehtoinen)

        mvn clean package wildfly:deploy

2. Paketin poisto palvelimelta:

        mvn wildfly:undeploy

3. Poisto ja uudelleenlevitys

        mvn wildfly:redeploy
 
 
 ## Harjoitukset
 
Katso myös docs-hakemisto

Varsinkin Arquillian testien kanssa on syytä käyttää profiileja ajettaessa, eli esim.

	mvn clean verify -P arq-wildfly-managed

 
 # Käytetyt APIt
 
 Tässä Java EE -palvelinsovelluksessa on käytössä seuraavia Java EE API osia:
 
 - EJB
     1. EJB
     2. JPA - Entityluokkia ja repository, tietokantana H2
     3. Events - laukaisu
     4. CDI
 - Web
     1. JAX-RS - Kontrolleri
     2. Events - kuuntelu
     2. Web Socket - Uuden kirjan luomisesta ilmoitus kaikille asiakaille
     3. Validation
     4. CDI
 
