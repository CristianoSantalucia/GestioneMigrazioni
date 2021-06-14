package it.polito.tdp.borders.model;

public class Evento implements Comparable<Evento>
{
	private int t ; //tempo 
	private Country cOrigine ;  
	private Country cDestinazione ;  
	private int qnt; 
	
	private boolean analizza ; 
	
	public Evento(int t, Country cOrigine, Country cDestinazione, int qnt, boolean analizza)
	{
		this.t = t;
		this.cOrigine = cOrigine;
		this.cDestinazione = cDestinazione;
		this.qnt = qnt;
		
		this.analizza = analizza; 
		
		this.aggiornaQnt(cOrigine, cDestinazione, qnt); 
	}
	private void aggiornaQnt(Country cOrigine, Country cDestinazione, int qnt)
	{
		 cDestinazione.aumentaPopolazione(qnt);
		 cOrigine.diminuisciPopolazione(qnt);
	}
	
	public boolean isAnalizza()
	{
		return analizza;
	}
	public int getT()
	{
		return t;
	}
	public Country getcOrigine()
	{
		return cOrigine;
	}
	public Country getcDestinazione()
	{
		return cDestinazione;
	}
	public int getQnt()
	{
		return qnt;
	}
	@Override public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + t;
		return result;
	}
	@Override public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Evento other = (Evento) obj;
		if (t != other.t) return false;
		return true;
	}
	@Override public int compareTo(Evento other)
	{
		return this.t - other.t; 
	}
	
	
	@Override public String toString()
	{
		if(analizza)
			return String.format("%d : %s -> %s  [migranti: %d]", t,cOrigine.stampaInfo(),cDestinazione.stampaInfo(),qnt);
		else 
			return String.format("%d : %s -> %s  [migranti: %d] (NESSUNA MIGRAZIONE)", t,cOrigine.stampaInfo(),cDestinazione.stampaInfo(),qnt);
	}
}
