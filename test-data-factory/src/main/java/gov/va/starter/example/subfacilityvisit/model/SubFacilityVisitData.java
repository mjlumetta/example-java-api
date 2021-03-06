package gov.va.starter.example.subfacilityvisit.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SubFacilityVisitData {

  private String id;

  @NonNull private String userName;
  @NonNull private String pii;
  @NonNull private String firstName;
  @NonNull private String lastName;
  private String fullName;
}
