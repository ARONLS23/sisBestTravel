package com.arrows_ls.best_travel.domain.entities.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role {

    @Field(name = "granted_authorities")
    private Set<String> grantedAuthorities;

}
