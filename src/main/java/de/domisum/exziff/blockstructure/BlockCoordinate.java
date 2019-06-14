package de.domisum.exziff.blockstructure;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

// UTIL
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BlockCoordinate
{

	@Getter
	private final int x;

	@Getter
	private final int y;

	@Getter
	private final int z;

}
