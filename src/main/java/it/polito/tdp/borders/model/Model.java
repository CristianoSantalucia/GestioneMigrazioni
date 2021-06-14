package it.polito.tdp.borders.model;

/*
 * classe Model preimpostata questo documento è soggetto ai relativi diritti di
 * ©Copyright giugno 2021
 */

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;

import it.polito.tdp.borders.db.BordersDAO;

public class Model
{
	private BordersDAO dao;
	private Map<Integer, Country> vertici;
	private Graph<Country, DefaultEdge> grafo;

	public Model()
	{
		this.dao = new BordersDAO();
	}

	public void creaGrafo(int year)
	{
		// ripulisco mappa e grafo
		this.vertici = new HashMap<>();
		this.grafo = new SimpleGraph<>(DefaultEdge.class);

		/// vertici
		this.dao.getVertici(vertici, year);
		Graphs.addAllVertices(this.grafo, this.vertici.values());

		/// archi
		for (Adiacenza a : this.dao.getAdiacenze(vertici))
		{
			this.grafo.addEdge(a.getC1(), a.getC2());
		}
	}

	public String stampaGrafo()
	{
		List<Country> vertici = new ArrayList<>(this.grafo.vertexSet());
		vertici.sort((v1, v2) -> -(this.grafo.degreeOf(v1) - this.grafo.degreeOf(v2)));

		String s = "";
		s += "\n\n**STAMPA GRAFO**";
		for (Country c : vertici)
		{
			s += String.format("\n%s (%d)", c, this.grafo.degreeOf(c));
		}
		return s;
	}

	public int getNumVertici()
	{
		return this.grafo.vertexSet().size();
	}

	public int getNumArchi()
	{
		return this.grafo.edgeSet().size();
	}

	public Collection<Country> getVertici()
	{
		List<Country> vertici = new ArrayList<>(this.grafo.vertexSet());
		vertici.sort((v1, v2) -> v1.getStateName().compareTo(v2.getStateName()));
		return vertici;
	}

	public Collection<DefaultEdge> getArchi()
	{
		return this.grafo.edgeSet();
	}

	// SIMULAZIONE

	private static final Integer numTotMigranti = 1000;
	PriorityQueue<Evento> eventi;
	int t = 0;

	public void simula(Country partenza)
	{
		this.eventi = new PriorityQueue<>();
		this.eventi.add(new Evento(t, new Country(0, "NULL", "NULL"), partenza, numTotMigranti, true));

		while (!this.eventi.isEmpty())
		{
			Evento e = eventi.poll();
			System.out.println(e);

			if (e.isAnalizza())
			{
				Country inAnalisi = e.getcDestinazione();

				// divisione al 50% tra stanziali e non
				int nonStanziali = (int) e.getQnt() / 4;

				List<Country> vicini = new ArrayList<>(Graphs.neighborListOf(this.grafo, inAnalisi));
				if (vicini.size() < nonStanziali)
				{
					int numStati = vicini.size();
//				if( nonStanziali / numStati != (int) numStati / nonStanziali) //ha parte decimale
//					nonStanziali--; 
					double singoloGruppo = nonStanziali / numStati;
					int t2 = ++t;
					for (int c = 0; c < vicini.size(); c++)
					{
						Evento migrazione = new Evento(t2, inAnalisi, vicini.get(c), (int) singoloGruppo, true);
						this.eventi.add(migrazione); 
					}
				}
				else
				{
					int t2 = ++t;
					Evento migrazione = new Evento(t2, inAnalisi, inAnalisi, nonStanziali, false);
					eventi.add(migrazione);
				}
			}
		}

		System.out.println("\n\n T = " + this.t);
		for (Country c : this.grafo.vertexSet())
		{
			if (c.getPopolazione() > 1)
			{
				System.out.println("INFO STATO: " + c.stampaInfo());
			}
		}

	}
}
