package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class BudgetTemplateAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBudgetTemplateAllPropertiesEquals(BudgetTemplate expected, BudgetTemplate actual) {
        assertBudgetTemplateAutoGeneratedPropertiesEquals(expected, actual);
        assertBudgetTemplateAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBudgetTemplateAllUpdatablePropertiesEquals(BudgetTemplate expected, BudgetTemplate actual) {
        assertBudgetTemplateUpdatableFieldsEquals(expected, actual);
        assertBudgetTemplateUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBudgetTemplateAutoGeneratedPropertiesEquals(BudgetTemplate expected, BudgetTemplate actual) {
        assertThat(expected)
            .as("Verify BudgetTemplate auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBudgetTemplateUpdatableFieldsEquals(BudgetTemplate expected, BudgetTemplate actual) {
        assertThat(expected)
            .as("Verify BudgetTemplate relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getStartDate()).as("check startDate").isEqualTo(actual.getStartDate()))
            .satisfies(e -> assertThat(e.getEndDate()).as("check endDate").isEqualTo(actual.getEndDate()))
            .satisfies(
                e -> assertThat(e.getEstimatedDurationDays()).as("check estimatedDurationDays").isEqualTo(actual.getEstimatedDurationDays())
            )
            .satisfies(e -> assertThat(e.getDurationMonths()).as("check durationMonths").isEqualTo(actual.getDurationMonths()))
            .satisfies(
                e ->
                    assertThat(e.getMonthlyHours())
                        .as("check monthlyHours")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getMonthlyHours())
            )
            .satisfies(
                e ->
                    assertThat(e.getPlannedHours())
                        .as("check plannedHours")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getPlannedHours())
            )
            .satisfies(e -> assertThat(e.getResourceCount()).as("check resourceCount").isEqualTo(actual.getResourceCount()))
            .satisfies(e -> assertThat(e.getIncome()).as("check income").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getIncome()))
            .satisfies(
                e ->
                    assertThat(e.getOtherTaxes())
                        .as("check otherTaxes")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getOtherTaxes())
            )
            .satisfies(
                e -> assertThat(e.getDescriptionOtherTaxes()).as("check descriptionOtherTaxes").isEqualTo(actual.getDescriptionOtherTaxes())
            )
            .satisfies(
                e ->
                    assertThat(e.getWithholdingTaxes())
                        .as("check withholdingTaxes")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getWithholdingTaxes())
            )
            .satisfies(
                e ->
                    assertThat(e.getModAndCifCosts())
                        .as("check modAndCifCosts")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getModAndCifCosts())
            )
            .satisfies(
                e ->
                    assertThat(e.getGrossProfit())
                        .as("check grossProfit")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getGrossProfit())
            )
            .satisfies(
                e ->
                    assertThat(e.getGrossProfitPercentage())
                        .as("check grossProfitPercentage")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getGrossProfitPercentage())
            )
            .satisfies(
                e ->
                    assertThat(e.getGrossProfitRule())
                        .as("check grossProfitRule")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getGrossProfitRule())
            )
            .satisfies(
                e ->
                    assertThat(e.getAbsorbedFixedCosts())
                        .as("check absorbedFixedCosts")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getAbsorbedFixedCosts())
            )
            .satisfies(
                e ->
                    assertThat(e.getOtherExpenses())
                        .as("check otherExpenses")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getOtherExpenses())
            )
            .satisfies(
                e ->
                    assertThat(e.getProfitBeforeTax())
                        .as("check profitBeforeTax")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getProfitBeforeTax())
            )
            .satisfies(
                e ->
                    assertThat(e.getEstimatedTaxes())
                        .as("check estimatedTaxes")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getEstimatedTaxes())
            )
            .satisfies(
                e ->
                    assertThat(e.getEstimatedNetProfit())
                        .as("check estimatedNetProfit")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getEstimatedNetProfit())
            )
            .satisfies(
                e ->
                    assertThat(e.getNetMarginPercentage())
                        .as("check netMarginPercentage")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getNetMarginPercentage())
            )
            .satisfies(
                e ->
                    assertThat(e.getNetMarginRule())
                        .as("check netMarginRule")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getNetMarginRule())
            )
            .satisfies(
                e ->
                    assertThat(e.getCommissionToReceive())
                        .as("check commissionToReceive")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getCommissionToReceive())
            )
            .satisfies(e -> assertThat(e.getActive()).as("check active").isEqualTo(actual.getActive()))
            .satisfies(e -> assertThat(e.getCreatedBy()).as("check createdBy").isEqualTo(actual.getCreatedBy()))
            .satisfies(e -> assertThat(e.getCreatedDate()).as("check createdDate").isEqualTo(actual.getCreatedDate()))
            .satisfies(e -> assertThat(e.getLastModifiedBy()).as("check lastModifiedBy").isEqualTo(actual.getLastModifiedBy()))
            .satisfies(e -> assertThat(e.getLastModifiedDate()).as("check lastModifiedDate").isEqualTo(actual.getLastModifiedDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBudgetTemplateUpdatableRelationshipsEquals(BudgetTemplate expected, BudgetTemplate actual) {
        assertThat(expected)
            .as("Verify BudgetTemplate relationships")
            .satisfies(e -> assertThat(e.getCountry()).as("check country").isEqualTo(actual.getCountry()));
    }
}
