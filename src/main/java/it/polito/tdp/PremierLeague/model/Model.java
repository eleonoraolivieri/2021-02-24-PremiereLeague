package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;






public class Model {
	

	private SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge> grafo;
	private  PremierLeagueDAO dao;
	private Map<Integer,Player> idMap;
	private List<Player> players;
	private List<Match> matches;


	public Model() {
		
		dao = new  PremierLeagueDAO();
		idMap = new HashMap<Integer,Player>();
		dao.listAllPlayers(idMap);
		
	}
	
	public List<Match> getMatch() {
		if(this.matches==null) {
			PremierLeagueDAO dao = new PremierLeagueDAO() ;
			this.matches = dao.listAllMatches() ;
		}
		
		return this.matches ;
	}
	
	public void creaGrafo(Match match) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		players = this.dao.getVertici(match,this.idMap);
		//aggiungo i vertici
		
		Graphs.addAllVertices(this.grafo, players);
		
		//aggiungo gli archi
		for(Arco a : dao.getArchi(match,idMap)) {
			if(this.grafo.containsVertex(a.getV1()) && 
					this.grafo.containsVertex(a.getV2())) {
				DefaultWeightedEdge e = this.grafo.getEdge(a.getV1(), a.getV2());
				if(e == null) {
					Graphs.addEdgeWithVertices(grafo, a.getV1(), a.getV2(), a.getPeso());
				
					}
			}

	}
		
		System.out.println("VERTICI: " +this.grafo.vertexSet().size());
		System.out.println("ARCHI: " +this.grafo.edgeSet().size());
		
		
			
	}
	
	public Player giocatoreMigliore() {
		double max = 0.0;
		Player result = null ;
		
		for(Player p: this.grafo.vertexSet()) {
			double val = 0.0;
			for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(p))
				val += this.grafo.getEdgeWeight(e) ;
			for(DefaultWeightedEdge e: this.grafo.incomingEdgesOf(p))
				val -= this.grafo.getEdgeWeight(e) ;
			
			if(val>max) {
				max = val ;
				result = p ;
			}
		}
		return result; 
		
		
	}
	
	public double deltaGiocatoreMigliore() {
		double max = 0.0;
		Player result = null ;
		
		for(Player p: this.grafo.vertexSet()) {
			double val = 0.0;
			for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(p))
				val += this.grafo.getEdgeWeight(e) ;
			for(DefaultWeightedEdge e: this.grafo.incomingEdgesOf(p))
				val -= this.grafo.getEdgeWeight(e) ;
			
			if(val>max) {
				max = val ;
				result = p ;
			}
		}
		return max; 
	}
	

	

	
	public int getNVertici(){
		return this.grafo.vertexSet().size();
	}
	
	
	/**
	 * Metodo che restituisce il numero di archi del grafo
	 * @return
	 */
	public int getNArchi(){
		return this.grafo.edgeSet().size();
	}
	
	public List<Player> getVertici(){
		return new ArrayList<Player>(this.grafo.vertexSet());
	}
	
	
}
