package org.nmng.library.manager.model;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import org.nmng.library.manager.dto.request.BookSearchDto;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class BookSearchModel extends EntitySearchModel<BookSearchDto> {
    public BookSearchModel(BookSearchDto searchDTO) {
        super(searchDTO);
    }

    // TODO: refactor this
    @Override
    protected Map<String, Condition> constructsCriteria(BookSearchDto searchDTO) {
        Map<String, Condition> stringCriteria = new HashMap<>();
        // map DTO field names to entity's field
//        if (searchDTO.getId() != null)
//            stringCriteria.put("id", formatCondition("id", searchDTO.getId()));

        if (searchDTO.getName() != null)
            stringCriteria.put("name", formatCondition("name", searchDTO.getName()));
//        if (searchDTO.getCategory() != null)
//            stringCriteria.put("category", formatCondition("category", searchDTO.getCategory()));

        if (searchDTO.getAuthors() != null)
            stringCriteria.put("authors", formatCondition("authors", searchDTO.getAuthors()));

        if (searchDTO.getBookNumber() != null)
            stringCriteria.put("bookNumber", formatCondition("bookNumber", searchDTO.getBookNumber()));

//        if (searchDTO.getEdition() != null)
//            stringCriteria.put("edition", formatCondition("edition", searchDTO.getEdition()));

        if (searchDTO.getPublisher() != null)
            stringCriteria.put("publisher", formatCondition("publisher", searchDTO.getPublisher()));

        return stringCriteria;
    }

    @Override
    protected Expression<?> createExpression(CriteriaBuilder builder, Condition condition) {
        String attributeName = condition.getAttributeName();
        String value = condition.getValue();

        return switch (attributeName) {
            case "id", "bookNumber", "edition" -> builder.literal(Integer.parseInt(value)); // Expression<Integer>
            default -> condition.getOperator().equals("like") || condition.getOperator().equals("=")
                    ? builder.literal("%" + value + "%")
                    : builder.literal(value); // Expression<String>
        };
    }

    @Override
    protected Class<BookSearchDto> getSearchDtoClass() {
        return BookSearchDto.class;
    }
}
