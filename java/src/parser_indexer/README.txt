README

parser_indexer


- Parser
	Program pre parsovanie freebase databazy. Vstupom je freebase v komprimovanej podobe. 
	V zdrojovom kode je potrebne definovat cestu k vstupnemu suboru a definovat vysupny subor.

	Vstup: 	freebase.gz
	vystup: parsed_freebase.xml
	

- Helpers
	Vyparsovane data je potrebne opravit (aj koli chybe v Jave - vystup v UTF-8).
	EscapeAmpInXML.java zabezpecuje odstranenie neziaducich znakov a nahradenie znaku & za &amp;

	DocumentHelper.java poskytuje toString metodu pre Lucene Document
	

- Indexer
	Zo vstupneho XML (opraveneho pomocou EscapeAmpInXML.java) vytvara Lucene index.
	Index je vytvoreny ako zlozka, jej nazov je mozne upravit v zdrojovom kode.

	Indexer.java - obaluje funkcionalitu indexovania
	SaxReader - cita XML a pri zachyteni udalosti (napr. zaciatok noveho elementu) vola EventHandler
	CustomHandler - spracovava udalosti zo SaxParseru. Na zaklade typu elementu vykona pristlusnu akciu nad Lucene Document, pripadne vlozi ho do indexu	
	LuceneIndexBuilder - ma na staroti vytvorenie a naplnanie indexu

- Searcher
	Nastroj pre vyhladavanie nad vytvorenym indexom. Nacita vytvoreny index a dopytuje sa nad nim.
	Vyhladavanie je riesene cez SearchWindow.java, umoznuje zadat viaclovny dopyt (akcepruje aj *) a
	zvolit pre aky atribut sa ma dane hladanie vztahovat. Vysledky zobrazi v textovom poli (max 200) zo vsetkych najdenych. 


