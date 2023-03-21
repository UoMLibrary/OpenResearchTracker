package uk.ac.man.library.openresearchtracker.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import uk.ac.man.library.openresearchtracker.entities.Publication;

public interface PublicationService {

	void save(Publication publication);
	
	Page<Publication> publicationFilter(String title, String pureStatus,
			String complianceStatus, String oa_route, 
			String funder_Wellcome, 
			String funder_CRUK, 
			String funder_BHF, 
			String funder_NIHR, 
			String funder_ERC, 
			String funder_UKRI,String facultyName, String schoolName, Pageable pageable);
	
	List<Publication> publicationFilter(String title, String pureStatus,
			String complianceStatus, String oa_route, 
			String funder_Wellcome, 
			String funder_CRUK, 
			String funder_BHF, 
			String funder_NIHR, 
			String funder_ERC, 
			String funder_UKRI,String facultyName, String schoolName);
	
	void deleteAll();
	
	Optional<Publication> findByPureId(String pureId);
}
