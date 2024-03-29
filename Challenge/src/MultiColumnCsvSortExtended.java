import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

public class MultiColumnCsvSortExtended {
	private static final String COLUMN_SEPARATOR = ",";

	public static void main(String[] args) throws Exception
	{
		InputStream inputStream = new FileInputStream("/Users/wawinsathyamoorthy/Desktop/input-data.csv");
		List<List<String>> lines = readCsv(inputStream);
		lines = lines.subList(lines.size() -3, lines.size());

		// Create a comparator that compares the elements from column 0,
		// in ascending order
		Comparator<List<String>> c0 = createDesendingComparator(2);

		// Create a comparator that compares the elements from column 2,
		// in descending order
		Comparator<List<String>> c1 = createDesendingComparator(5);


		// Create a comparator that compares primarily by using c0,
		// and secondarily by using c1
		Comparator<List<String>> comparator = createComparator(c1, c0);
		Collections.sort(lines, comparator);


		OutputStream outputStream = new FileOutputStream("output.csv");
		String header = "ENTRY_TYPE,Plantingdate,GID,DESIGNATION,ENTRY_NO,EH_cm,PH_cm,DTA_days__obs,DTS_days__obs,Moi_pct__STD,GW_g__ FieldWB,EarsHvst_ears_plot,RLodg_pl_plot,SLodg_pl_plot,REP_NO,PLOT_NO,column,row ";
		writeCsv( header, lines, outputStream);
		System.out.println();

	}

	private static List<List<String>> readCsv(
			InputStream inputStream) throws IOException
	{
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(inputStream));
		List<List<String>> lines = new ArrayList<List<String>>();

		String line = null;

		//Skip header

		line = reader.readLine();

		while (true)
		{
			line = reader.readLine();
			if (line == null)
			{
				break;
			}
			List<String> list = Arrays.asList(line.split(COLUMN_SEPARATOR));
			lines.add(list);
		}
		return lines;
	}

	private static void writeCsv(
			String header,List<List<String>> lines, OutputStream outputStream)
			throws IOException
	{
		Writer writer = new OutputStreamWriter(outputStream);
		writer.write(header+"\n");
		for (List<String> list : lines)
		{
			for (int i = 0; i < list.size(); i++)
			{
				writer.write(list.get(i));
				if (i < list.size() - 1)
				{
					writer.write(COLUMN_SEPARATOR);
				}
			}
			writer.write("\n");
		}
		writer.close();

	}

	@SafeVarargs
	private static <T> Comparator<T>
	createComparator(Comparator<? super T>... delegates)
	{
		return (t0, t1) ->
		{
			for (Comparator<? super T> delegate : delegates)
			{
				int n = delegate.compare(t0, t1);
				if (n != 0)
				{
					return n;
				}
			}
			return 0;
		};
	}

	private static <T extends Comparable<? super T>> Comparator<List<T>>
	createAscendingComparator(int index)
	{
		return createListAtIndexComparator(Comparator.naturalOrder(), index);
	}

	private static <T extends Comparable<? super T>> Comparator<List<T>>
	createDesendingComparator(int index)
	{
		return createListAtIndexComparator(Comparator.reverseOrder(), index);
	}

	private static <T> Comparator<List<T>>
	createListAtIndexComparator(Comparator<? super T> delegate, int index)
	{
		return (list0, list1) ->
				delegate.compare(list0.get(index), list1.get(index));
	}


}