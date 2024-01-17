package at.if22b208.mtc.util.mapper;

import at.if22b208.mtc.dto.card.CardDto;
import at.if22b208.mtc.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CardMapper {
    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    CardDto map(Card card);
}