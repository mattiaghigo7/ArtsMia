package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<ArtObject,DefaultWeightedEdge> graph;
	private List<ArtObject> allNodes;
	private ArtsmiaDAO dao;
	private Map<Integer,ArtObject> idMap;

	public Model() {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.allNodes = new ArrayList<>();
		this.dao = new ArtsmiaDAO();
		this.idMap = new HashMap<>();
	}
	
	public void loadNodes(){
		this.allNodes = dao.listObjects();
		for(ArtObject a : allNodes) {
			this.idMap.put(a.getId(), a);
		}
	}
	
	public void buildGraph() {
		this.loadNodes();
		
		Graphs.addAllVertices(this.graph, this.allNodes);
		
//		for(ArtObject a1 : this.allNods) {
//			for(ArtObject a2 : this.allNods) {
//				int peso = this.dao.getWeght(a1.getId(), a2.getId());
//				Graphs.addEdgeWithVertices(this.graph, a1, a2, peso);
//			}
//		}
		
		List<edgeModel> allEdges = this.dao.getAllWeights(idMap);
		
		for(edgeModel e : allEdges) {
			Graphs.addEdgeWithVertices(this.graph, e.getSource(), e.getTarget(), e.getPeso());
		}
		
		System.out.println("This graph contains "+this.graph.vertexSet().size()+" nodes.");
		System.out.println("This graph contains "+this.graph.edgeSet().size()+" edges.");
	}
	
	public boolean isIdInGrapgh(Integer objId) {
		if(this.idMap.containsKey(objId)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Integer calcolaConnessa(Integer objId) {
		DepthFirstIterator<ArtObject, DefaultWeightedEdge> iterator = new DepthFirstIterator<>(this.graph,this.idMap.get(objId));
		List<ArtObject> compConnessa = new ArrayList<>();
		while(iterator.hasNext()) {
			compConnessa.add(iterator.next());
		}
		
//		ConnectivityInspector<ArtObject, DefaultWeightedEdge> inspector = new ConnectivityInspector<>(this.graph);
//		Set<ArtObject> setConnesso = inspector.connectedSetOf(this.idMap.get(objId));
		
		return compConnessa.size();
//		return setConnesso.size();
	}
}
