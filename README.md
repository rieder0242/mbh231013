# Prim Teszt

Ezt a projektet az MBH Bank felvételihez készítettem.

Ez (a Hello Word után) az első kotlin projektem.

## Végpontok

### A `/` végpont

Itt egy apró felület van teszteléshez

### A `/searching/start/{$count}` végpont

Itt lehet elindítani  `{$count}` szálon a keresést.

### A `/searching/stop}` végpont

Itt lehet leállítani a keresést.

### A `/prim/list/{$from}/{$to}` végpont

Itt lehet keresést indítani a [`{$from}` - `{$to}`] inkluziv tartományon. 

## Config

A futtatás helyén lévő `conf.properties` olvassa be az alkalmazás indulásakor.
Ebben két érték lehet: A `maxResponseCount`, ami azt adja meg maximum hány prímet ad válaszul a rendszer.
És a `maxThreadCount` ami a keresőszálak maximális értéke.

Ha hiányzik a file, vagy valamelyik érték, akkor a rendszer a default-ot hassználja.

## Specifikáció

Készíts egy Spring Boot-os alkalmazást webes felülettel (akár a swagger UI felületről hívható rest-es végpontok is megfelelőek), ami prímszámokat keres egy háttérfolyamatban.
A következő módon kell ezt megvalósítani:
* a prímszámkeresést egy rest-es interface-n (felületről) keresztül kiadott parancs segítségével lehessen indítani
* a prímszámkeresésnek a háttérben kell futnia egy vagy több szálon
* a megtalált prímszámokat mentsük, a legközelebbi keresésig őrizzük meg a program futása során
* legyen lehetőség lekérdezni egy intervallumban megtalált prímszámokat
* rest-es interface-n keresztül legyen lehetőség megállítani a prímszámkeresést
* az alkalmazás build-elését gradle-el valósítsuk meg
* min. java 11-es verziót használjunk, de javasolt a java 17
* preferált nyelv a kotlin

 Az alábbi rest-es interface-eket kell megvalósítani:
* prímszám keresés indítása szolgáltatás:
  * amennyiben többszálú prímszámkeresést valósítunk meg, akkor paraméterben meg lehessen adni a szálak számát, a megadott értéket ilyenkor le kell vizsgálni, hogy a nem lépi túl a konfigurációban megadott maximum szálak számát (pl.: 5)
  * elindítja háttérben a prímszámkeresést és azonnal visszaválaszol, hogy elindult a folyamat
  * amennyiben már fut a prímszámkeresés, hibaüzenettel azonnal válaszol, hogy már fut a folyamat
* megtalált prímszámokat kilistázó rest-es szolgáltatás:
  * két paramétert vár: minimum érték, maximum érték, a két érték között lévő összes megtalált prímszámot adja vissza. A lekérdezésben legyen védelem a túl sok találat kikérése ellen
  * amennyiben egy olyan intervallumot ad meg a felhasználó, ahol még nem futott a prímszámkeresés, akkor adjon vissza hibaüzenetet a lekérdezés
  * a lekérdezésnek akkor is működnie kell, ha a prímszámkeresés éppen fut és akkor is, miután le lett állítva
* prímszámkeresés leállítása szolgáltatás
  * állítsa le a háttérben a prímszámkereséshez tartozó szálakat 

## Megjegyzések

Azt, hogy `a megtalált prímszámokat mentsük, a legközelebbi keresésig őrizzük meg a program futása során`,
én nem úgy értem mintha a prímszámokat törölni kéne, bár a mondat azt is jelenthetné, de a valóságban nem rendelkezik arról hogy mit tegyünk a prímszámokkal a következő keresés inditásakor.
