package it.polito.tdp.borders.db;

import it.polito.tdp.borders.model.Adiacenza;
import it.polito.tdp.borders.model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BordersDAO
{

	public List<Country> loadAllCountries(Map<Integer, Country> countriesMap)
	{
		String sql = "SELECT ccode,StateAbb,StateNme " + "FROM country " + "ORDER BY StateAbb ";

		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			List<Country> list = new LinkedList<Country>();

			while (rs.next())
			{

				if (countriesMap.get(rs.getInt("ccode")) == null)
				{

					Country c = new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
					countriesMap.put(c.getcCode(), c);
					list.add(c);
				}
				else list.add(countriesMap.get(rs.getInt("ccode")));
			}
			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public void getVertici(Map<Integer, Country> vertici, int year)
	{
		String sql = "SELECT co.* "
				+ "FROM contiguity c, country co "
				+ "WHERE c.state1no = co.CCode "
				+ "		AND c.conttype = 1 "
				+ "		AND c.year = ? "
				+ "GROUP BY co.CCode ";

		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			ResultSet rs = st.executeQuery();

			while (rs.next())
			{
				Country c = new Country(rs.getInt("CCode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				vertici.putIfAbsent(c.getcCode(), c); 
			}
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public Collection<Adiacenza> getAdiacenze(Map<Integer, Country> vertici)
	{
		String sql = "SELECT c.state1no c1, c.state2no c2 "
					+ "FROM contiguity c "
					+ "WHERE c.state1no < c.state2no "
					+ "		AND c.conttype = 1 "; 

		List<Adiacenza> list = new LinkedList<>();
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next())
			{
				Country c1 = vertici.get(rs.getInt("c1")); 
				Country c2 = vertici.get(rs.getInt("c2")); 
				if (c1 != null && c2 != null)
				{
					Adiacenza a = new Adiacenza(c1, c2); 
					if(!list.contains(a))
						list.add(a); 
				}
			}
			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
