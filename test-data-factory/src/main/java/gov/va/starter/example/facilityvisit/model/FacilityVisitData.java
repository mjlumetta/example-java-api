package gov.va.starter.example.facilityvisit.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FacilityVisitData {

  private String id;

  @NonNull private String userName;
  @NonNull private String pii;
  @NonNull private String firstName;
  @NonNull private String lastName;
  private String fullName;
}
