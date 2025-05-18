# ORECH - Interaktívna Mapa pre Počítačovú Hru

## Prehľad Projektu
ORECH je mobilná aplikácia navrhnutá pre fiktívnu open-world zónu "Chenyu Vale" v počítačovej hre. Poskytuje interaktívnu mapu, ktorá pomáha hráčom lokalizovať a sledovať herné materiály, ktoré sú kľúčové pre progres v hre, ale často nie sú označené priamo v hernej mape.

## Funkcie
- **Interaktívna Mapa**: Preskúmajte detailnú mapu Chenyu Vale s možnosťou priblíženia a posúvania
- **Vyhľadávanie Materiálov**: Vyhľadávajte materiály podľa názvu alebo lokácie
- **Sledovanie Materiálov**: Označte nazbierané materiály a sledujte čas ich respawnu
- **Správa Zdrojov**: Zobrazte zoznam všetkých sledovaných materiálov s ich dostupnosťou
- **Perzistencia**: Lokálne úložisko zabezpečuje, že vaše údaje o sledovaní sú uložené medzi spusteniami

## Technická Implementácia
Aplikácia je postavená pomocou:
- Kotlin s Jetpack Compose pre moderný vývoj používateľského rozhrania
- Navigation Compose pre typovo bezpečnú navigáciu medzi obrazovkami
- SharedPreferences s JSON serializáciou pre perzistentné ukladanie dát
- Reaktívne aktualizácie UI pomocou Kotlin Flow

## Obrazovky
1. **Hlavné Menu**: Navigačný hub s tlačidlami pre Mapu, Vyhľadávanie a Zoznam Materiálov
2. **Mapa**: Interaktívna mapa sveta Chenyu Vale
3. **Vyhľadávanie**: Nájdite materiály podľa názvu s filtráciou v reálnom čase
4. **Detaily Materiálu**: Zobrazte lokácie konkrétnych materiálov a označte ich ako nazbierané
5. **Sledované Materiály**: Monitorujte dostupnosť a čas respawnu nazbieraných materiálov

## Prípady Použitia
- Prezerajte mapu pre preskúmanie herného sveta
- Vyhľadávajte konkrétne materiály pre ich lokalizáciu na mape
- Sledujte nazbierané materiály pre informácie o ich opätovnej dostupnosti
- Spravujte zbieranie zdrojov zobrazením všetkých sledovaných materiálov na jednom mieste
