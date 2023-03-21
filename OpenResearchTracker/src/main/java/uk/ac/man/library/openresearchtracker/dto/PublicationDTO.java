package uk.ac.man.library.openresearchtracker.dto;

import java.util.List;

import uk.ac.man.library.openresearchtracker.entities.Publication;

public class PublicationDTO {
	
	private int draw;
	private Long recordsTotal;
	private Long recordsFiltered;
	
	private List<Publication> data;

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public Long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public Long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<Publication> getData() {
		return data;
	}

	public void setData(List<Publication> data) {
		this.data = data;
	}

}
