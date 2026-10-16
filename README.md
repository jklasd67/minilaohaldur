# Poe laohaldus

Java konsoolirakendus väikese poe laoseisu haldamiseks. Kasutaja saab kuvada kõik tooted, lisada toidu- või elektroonikatoote, suurendada ja vähendada kogust, otsida nime järgi ning arvutada kogu lao väärtust.


## Käivitamine

Vaja on **JDK 17 või uuemat**, mis sisaldab nii `java` kui ka `javac` käsku. Väliseid Java teeke, Mavenit ega Gradle'it pole vaja. Ava terminal projekti juurkaustas.

```sh
java -version
javac -version
mkdir -p out
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

Windows PowerShellis kasuta kausta loomiseks `New-Item -ItemType Directory -Force out` ja kompileerimiseks `javac -encoding UTF-8 -d out (Get-ChildItem src/*.java).FullName`. Käivituskäsk on sama. Kasuta UTF-8 toega terminali, et täpitähed ja euro märk oleksid loetavad.

Kui muudad lähtekoodi, kompileeri enne järgmist käivitamist uuesti. Kui käsku ei leita, kontrolli JDK paigaldust ja `PATH`-i; kui põhiklassi ei leita, kontrolli, et oled projekti juurkaustas ja kompileerimine õnnestus.


## Kasutamine

| Valik | Tegevus |
| --- | --- |
| 1 | Kõik tooted koos hinna, koguse ja alamklassi lisainfoga |
| 2 | Uue toidu- või elektroonikatoote lisamine |
| 3 | Koguse muutmine toote täpse nime järgi; `5` lisab viis, `-2` eemaldab kaks |
| 4 | Kõik tooted, mille nimi sisaldab otsitavat teksti |
| 5 | Lao koguväärtus: iga toote hind × kogus, seejärel tulemuste summa |
| 0 | Programmist väljumine |

Alguses on laos Piim (1.50 €, 10 tk), Leib (2.00 €, 5 tk) ja Veekeetja (25.00 €, 2 tk). Algne koguväärtus on **75.00 €**.

Otsing ja täpse nime kontroll ei erista suur- ega väiketähti. Näiteks `PI` leiab Piima. Sama nimega teist toodet lisada ei saa. Hind võib sisaldada punkti või koma. Tühi sisend, vigased arvud, negatiivne hind ja negatiivne algkogus ei sobi. Koguse vähendamine alla nulli ning `int`-vahemiku ületamine annavad veateate ja jätavad koguse muutmata. Puuduva nimega toote muutmine annab samuti veateate. Sisendi lõpp lõpetab programmi rahulikult.

Andmed asuvad ainult mälus: sulgemisel kaovad lisatud tooted ja muudatused ning järgmisel käivitamisel taastuvad kolm algtoodet. Faili või andmebaasi salvestamist ülesanne ei nõua.

## Projekti failid

| Fail | Vastutus |
| --- | --- |
| [Product.java](src/Product.java) | Abstraktne toode, ühised väljad, valideerimine ja laoväärtus |
| [FoodProduct.java](src/FoodProduct.java) | Toidutoode koos säilitamisjuhisega |
| [ElectronicProduct.java](src/ElectronicProduct.java) | Elektroonikatoode koos garantiikuudega |
| [Inventory.java](src/Inventory.java) | Loend, lisamine, otsimine, koguse muutmine ja summa |
| [Main.java](src/Main.java) | Menüü ning kasutaja sisendi lugemine |
| [InventoryTest.java](tests/InventoryTest.java) | 22 sõltuvusteta automaatkontrolli |
| [demo.html](evidence/demo.html) | Brauseris avatav terminalisalvestus |

## Neli objektorienteeritud põhimõtet

Järgmised selgitused on õppimise toeks. Harjuta samade kohtade selgitamist oma sõnadega.

**Kapseldamine:** `Product`-i `name`, `price` ja `quantity` on `private`. Väljast saab neid lugeda getteritega. Hinda muudab `setPrice()` ja kogust `changeQuantity()`, kus sisend kontrollitakse enne muutmist. Näiteks `product.quantity = -10` ei kompileeru. Toote nimi määratakse loomisel ja on `final`, mistõttu seda hiljem muuta ei saa. Nii ei saa laohaldus kogemata kontrollidest mööda minna. `Inventory.getProducts()` tagastab loendi muutmist keelava vaate; selle sees olevate toodete kontrollitud meetodeid saab endiselt kasutada.

**Pärilus:** `FoodProduct extends Product` ja `ElectronicProduct extends Product`. Alamklassid saavad ühise nime, hinna, koguse ja meetodid baasklassilt. `super(name, price, quantity)` kutsub välja baasklassi konstruktori. Ühist valideerimist pole vaja kahes kohas korrata.

**Abstraktsioon:** `Product` on `abstract` ja määrab üldise toote mõiste. `getExtraInfo()` kirjeldab, mida iga toode peab oskama, ilma ühise teostuseta. Otse `new Product(...)` kirjutada ei saa. `Inventory` saab siiski kasutada üldist `Product`-tüüpi, teadmata iga alamklassi eripärasid.

**Polümorfism:** `Main.printProducts()` läbib `List<Product>`-i ning kutsub iga objekti puhul `System.out.println(product)`. See käivitab `Product.toString()`-i, mis kutsub `getExtraInfo()`-t. Java valib tegeliku objekti järgi toidutoote või elektroonikatoote `@Override`-teostuse. Ühes loendis olevad eri tüüpi tooted annavad sama meetodikutse peale erineva lisainfo. Uue alamklassi lisamisel ei pea üldist kuvamistsüklit ümber kirjutama.

## Andmetüübid ja andmestruktuur

| Tüüp | Näide ja põhjus |
| --- | --- |
| `String` | Nimi ja säilitamisjuhis on tekst |
| `int` | Kogus, garantiikuud ja menüüvalik on täisarvud |
| `double` | Hind ja koguväärtus võivad sisaldada murdosa |
| `boolean` | `running` näitab, kas menüütsükkel peab jätkuma |
| `long` | Ajutine kogus liitmisel, et avastada `int`-i ületäitumine |
| `List<Product>` / `ArrayList<Product>` | Muutuva pikkusega toodete kogum |

`double` sobib selle õppeülesande jaoks, kuid kümnendmurdude binaarkujul hoidmine võib anda ümardusvigu. Kuvamisel kasutatakse kahte komakohta. Päris rahaarvestuses eelistaksin `BigDecimal`-i või täisarvulisi sente. Lahendus eeldab väikese poe tavapäraseid hindu; väga suurte `double`-väärtuste korrutis või summa võib ületäituda.

Valisin `ArrayList`-i, sest tooteid on vähe, neid on lihtne lisada ja lisamise järjekorras läbi käia. See on geneeriline kogum, mis hoiab viiteid eri tooteobjektidele. Välja tüüp on üldine `List`, tegelik teostus `ArrayList`.

`searchByName()` käib `for`-tsükliga tooted läbi, teisendab nime ja otsingu väiketähtedeks ning kontrollib `contains()`-iga osalist vastet. Vastused kogutakse uude loendisse. `findByExactName()` võrdleb täisnime `equalsIgnoreCase()`-iga ja tagastab leitud toote või `null`-i. Mõlemad otsingud on toodete arvu suhtes O(n); teksti võrdlemisel lisandub nimede pikkusest sõltuv töö. `addProduct()` kontrollib enne lisamist samanimelise toote puudumist, seega ka kogu lisamistoiming on O(n). Kõigi toodete kuvamine ja koguväärtuse arvutamine on O(n). Väikese lao puhul on see piisav ja lihtne selgitada.

## Lühike tööpäevik: selle lahenduse arendus

Esmalt valmis abstraktne `Product` koos väljade kontrollidega ning kaks alamklassi, mis kuvavad erinevat lisainfot. Seejärel lisandus `Inventory`, kus saab tooteid hoida, nime järgi otsida ning laoväärtust arvutada. Kolmandas etapis valmis konsoolimenüü koos vigase sisendi käsitlemisega ning kontrolliti koguse vähendamist, ületäitumist ja otsingut. Kõige rohkem tähelepanu vajas koguse muutmine nii, et ebaõnnestunud toiming jätaks vana väärtuse alles. Lõpuks kompileeriti ja käivitati programm JDK 17-ga ning salvestati tegelik terminaliseanss ja kirjutati kasutusjuhend.

## Käivitustõend

Ava [evidence/demo.html](evidence/demo.html) brauseris ja vajuta **Esita algusest**. Fail töötab kohalikult ilma veebiserveri või väliste teekideta. See esitab tegelikult käivitatud Java programmi salvestatud terminaliväljundit; tegemist ei ole interaktiivse Java rakendusega.

Salvestusel kuvatakse algtooted, lisatakse Õun hinnaga 0,80 € ja kogusega 6, otsitakse `ÕU`, vähendatakse Õuna kogust kahe võrra ning kuvatakse lao koguväärtus. Lõpptulemus on **78.20 €**, sest `75 + 0.80 × 4 = 78.20`.

Toorvorming on [demo.cast](evidence/demo.cast), mida saab esitada käsuga `asciinema play evidence/demo.cast`, kui asciinema on paigaldatud. Loetav tekst on [demo.txt](evidence/demo.txt). Salvestuse koostas AI arenduse kontrollimiseks. Soovi korral tee esitamiseks ka oma ekraanipilt või salvestus, kus oled rakenduse ise käivitanud.

## Kontrollimine

```sh
javac -encoding UTF-8 -Xlint:all -d out src/*.java tests/*.java
java -cp out InventoryTest
```

PowerShellis anna kompilaatorile failid kujul `(Get-ChildItem src/*.java, tests/*.java).FullName`.

Kontrollitud JDK 17-ga: kompileerimine lõppes hoiatusteta ja kõik **22 kontrolli** läbisid. Need katavad otsingu, laoväärtuse, erineva lisainfo, koguse suurendamise ja vähendamise, nullkoguse, vigased andmed, topeltnimed, loendi kaitse ning koguse ületäitumise. Kontrollid kasutavad `AssertionError`-it ja töötavad ilma `-ea` liputa.

Lisaks kontrolliti nelja konsoolistsenaariumi: toidutoote lisamine koos koma sisaldava hinnaga ja otsinguga; vigased sisendid, duplikaat, koguse muutmine ja puuduv otsinguvastus; elektroonikatoote lisamine; sisendi lõpp poole sisestamise pealt. Kõik lõppesid oodatud tulemusega.

## Esitluseks harjutamine

1. Käivita projekt ja selgita, miks `main()` loob `Main`-objekti ning kutsub `run()`-i.
2. Lisa mõlemat liiki toode ja selgita konstruktorit, `super`-kutset ning privaatseid välju.
3. Otsi nimeosa järgi ja näita, kus loend läbi käiakse. Ennusta, mis juhtub puuduva nime või teistsuguste suur- ja väiketähtedega.
4. Vähenda kogust nullini ja seejärel proovi veel üks eemaldada. Selgita, miks teine toiming ebaõnnestub ja andmed alles jäävad.
5. Arvuta väikese näite laoväärtus käsitsi ning võrdle programmi tulemusega.
6. Tee harjutusmuudatus, kompileeri uuesti ja kontrolli selle mõju. Näiteks lisa `Inventory`-sse meetod, mis kogub uude loendisse ainult `getQuantity() == 0` tooted, ning ühenda see uue menüüvalikuga. Muuda siis ka `readInt("Valik: ", 0, 5)` ülemist piiri.
7. Vaata `git log --oneline` abil üle arenduse etapid ning ava ühe muudatuse sisu käsuga `git show <commit>`.

Negatiivne kogus on juba keelatud. Otsa saanud toode annab praegu koguväärtusesse nulli, sest hind korrutatakse nulliga; eraldi filtreerimine seda tulemust ei muudaks. Kategooriafiltri harjutuseks võid lisada abstraktse `getCategory()` meetodi ning teostada selle mõlemas alamklassis.

## Kasutatud Git-commit'id

Arenduse kolm sisulist etappi:

| Commit | Sisu |
| --- | --- |
| `b75ec7e` | Kapseldatud tooteklass ja kaks alamklassi |
| `9f2fc18` | Laohaldus, nimeotsing ja koguväärtuse arvutus |
| `07fcd38` | Konsoolimenüü, sisendikontroll ja 22 automaatkontrolli |

Järgnev dokumentatsiooni commit lisab selle README ja käivitustõendid. Täielikku ajalugu näeb käsuga `git log --oneline`; need commit'id kirjeldavad AI abil valminud projekti tegelikke arendusetappe.
