package sk.stuba.fiit.freebase.hadoop;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;


public class TestParser {
	
	private static String expectedOutput = "Topic [id=0100c5g, subjectsToObjectsMap={type.object.type=[music.single, base.type_ontology.non_agent, music.recording, common.topic, base.type_ontology.abstract], type.object.name=[Pulska yö], common.topic.alias=[Pulska yo]}]\r\n" + 
			"Topic [id=0100j9wd, subjectsToObjectsMap={type.object.type=[common.topic, base.type_ontology.physically_instantiable, people.person, base.type_ontology.animate, base.type_ontology.agent], type.object.name=[Stephen Kwelio Chemlany]}]\r\n" + 
			"Topic [id=01016v4g, subjectsToObjectsMap={type.object.type=[base.type_ontology.non_agent, base.type_ontology.inanimate, tv.tv_series_episode, common.topic, base.type_ontology.abstract], type.object.name=[Nuthin' but a G Thang, Rene], common.topic.alias=[Ain't Nuthin' But A \\\"G\\\" Thang, Rene]}]\r\n" + 
			"Topic [id=0101d6x0, subjectsToObjectsMap={type.object.type=[base.type_ontology.non_agent, common.topic, film.film_festival_event, base.type_ontology.inanimate, film.film_screening_venue, time.event], type.object.name=[2005 Washington D.C. European Union Film Festival], common.topic.alias=[18th Annual European Union Film Festival]}]\r\n" + 
			"Topic [id=0101h5tp, subjectsToObjectsMap={type.object.type=[sports.pro_sports_played]}]\r\n" + 
			"Topic [id=0101hwm4, subjectsToObjectsMap={type.object.type=[common.topic, base.type_ontology.agent, base.type_ontology.physically_instantiable, base.type_ontology.animate, people.person], type.object.name=[Rebecca Leighton], common.topic.alias=[Rebecca Jane Leighton]}]\r\n" + 
			"Topic [id=0101m2vj, subjectsToObjectsMap={type.object.type=[base.type_ontology.physically_instantiable, base.type_ontology.agent, people.person, base.type_ontology.animate, common.topic, people.deceased_person, base.schemastaging.government_position_held_extra, government.politician], type.object.name=[Gene Hodges], common.topic.alias=[Eugene Hodges]}]\r\n" + 
			"Topic [id=0101r5tv, subjectsToObjectsMap={type.object.type=[base.type_ontology.physically_instantiable, base.type_ontology.agent, people.person, film.editor, common.topic, base.type_ontology.animate], type.object.name=[Deepak Pal], common.topic.alias=[Late Deepak Pal, Deepak Paul]}]\r\n" + 
			"Topic [id=0101zz_q, subjectsToObjectsMap={type.object.type=[fictional_universe.fictional_character, common.topic, film.film_character, base.type_ontology.abstract, base.type_ontology.non_agent], type.object.name=[Julián's father], common.topic.alias=[El padre de Julián]}]\r\n" + 
			"Topic [id=01027t48, subjectsToObjectsMap={type.object.type=[base.type_ontology.agent, base.type_ontology.physically_instantiable, base.type_ontology.animate, people.person, common.topic], type.object.name=[Keesha Myers Wear], common.topic.alias=[Keesha Annika Myers]}]\r\n" + 
			"Topic [id=01028y6z, subjectsToObjectsMap={type.object.type=[book.book, base.type_ontology.non_agent, book.written_work, common.topic, base.type_ontology.abstract, base.type_ontology.inanimate], type.object.name=[The Little Man from Archangel], common.topic.alias=[Le petit homme d'Arkhangelsk]}]\r\n" + 
			"Topic [id=0102fpt0, subjectsToObjectsMap={type.object.type=[government.politician, base.type_ontology.physically_instantiable, base.type_ontology.animate, base.type_ontology.agent, common.topic, people.person], type.object.name=[Keith Curry], common.topic.alias=[Keith D. Curry]}]\r\n" + 
			"Topic [id=0102y34v, subjectsToObjectsMap={type.object.type=[base.type_ontology.non_agent, film.film, common.topic, base.type_ontology.inanimate, base.type_ontology.abstract], type.object.name=[The Final Payoff], common.topic.alias=[I teliki apopliromi]}]\r\n" + 
			"Topic [id=0104_bvw, subjectsToObjectsMap={type.object.type=[base.type_ontology.inanimate, location.location, common.topic, aviation.aviation_waypoint], type.object.name=[ALOYI], common.topic.alias=[fix ALOYI, waypoint ALOYI, ALOYI fix, ALOYI waypoint]}]\r\n" + 
			"Topic [id=0104_dk9, subjectsToObjectsMap={type.object.type=[location.location, base.type_ontology.inanimate, aviation.aviation_waypoint, common.topic], type.object.name=[ALUND], common.topic.alias=[fix ALUND, ALUND fix, waypoint ALUND, ALUND waypoint]}]\r\n" + 
			"Topic [id=0104bnf4, subjectsToObjectsMap={type.object.type=[base.type_ontology.inanimate, film.film, base.type_ontology.non_agent, base.type_ontology.abstract, common.topic], type.object.name=[Black Gospel]}]\r\n" + 
			"Topic [id=0104cx_, subjectsToObjectsMap={type.object.type=[base.type_ontology.abstract, common.topic, base.type_ontology.non_agent, music.recording], type.object.name=[Mä joka päivä töitä teen], common.topic.alias=[Ma joka paiva toita teen]}]\r\n" + 
			"Topic [id=0104jp, subjectsToObjectsMap={type.object.type=[location.location, location.hud_foreclosure_area, base.type_ontology.inanimate, location.hud_county_place, location.dated_location, base.type_ontology.abstract, location.statistical_region, location.census_designated_place, common.topic], type.object.name=[Buna], common.topic.alias=[Buna, Texas, Jasper County / Buna CDP, Buna, Texas]}]\r\n" + 
			"Topic [id=0104qhv, subjectsToObjectsMap={type.object.type=[base.type_ontology.non_agent, music.recording, common.topic, base.type_ontology.abstract], type.object.name=[Brothers and Sisters], common.topic.alias=[Brothers and Sisters]}]\r\n";
	
	@Test
	public void testParser() throws IOException {
		File file = new File("data/freebase_test.gz");
		String output = "";
		for(Topic topic : new NTripletParser().parseGzFile(file)) {
			output += topic.toString() + "\r\n";
		}
		assertEquals(output, expectedOutput);
	}
	
}
