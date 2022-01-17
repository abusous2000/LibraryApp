package com.sample.libraryapplication.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AssetsStat {
    @SerializedName("type")
    @Expose
    var type: Int? = null

    @SerializedName("count")
    @Expose
    var count: Int? = null

    @SerializedName("fileSize")
    @Expose
    var fileSize: Int? = null

    @SerializedName("fileName")
    @Expose
    var fileName: String? = null
}

class TownInfo {
    @SerializedName("geoPointId")
    @Expose
    var geoPointId: Int? = null

    @SerializedName("townId")
    @Expose
    var townId: Int? = null

    @SerializedName("districtId")
    @Expose
    var districtId: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("districtName")
    @Expose
    var districtName: String? = null

    @SerializedName("arabicName")
    @Expose
    var arabicName: String? = null

    @SerializedName("hebrewName")
    @Expose
    var hebrewName: String? = null

    @SerializedName("mainImage")
    @Expose
    var mainImage: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("guestBookMessageBoardId")
    @Expose
    var guestBookMessageBoardId: Int? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("atlasPages")
    @Expose
    var atlasPages: Any? = null

    @SerializedName("is1948")
    @Expose
    var is1948: Boolean? = null

    @SerializedName("createDate")
    @Expose
    var createDate: String? = null

    @SerializedName("assetsStats")
    @Expose
    var assetsStats: List<AssetsStat>? = null

    @SerializedName("gpTown")
    @Expose
    var gpTown: GpTown? = null

    @SerializedName("town")
    @Expose
    var town: Town? = null

    @SerializedName("placeholder")
    @Expose
    var placeholder: Boolean? = null

    @SerializedName("israeli")
    @Expose
    var israeli: Boolean? = null
}

class GpTown {
    @SerializedName("arabicName")
    @Expose
    var arabicName: String? = null

    @SerializedName("featuredVideoUrl")
    @Expose
    var featuredVideoUrl: String? = null

    @SerializedName("featuredVideoUrl2")
    @Expose
    var featuredVideoUrl2: String? = null

    @SerializedName("group194URL")
    @Expose
    var group194URL: String? = null

    @SerializedName("distanceFromDistrict")
    @Expose
    var distanceFromDistrict: Double? = null

    @SerializedName("directionFromDistrict")
    @Expose
    var directionFromDistrict: String? = null

    @SerializedName("elevationFromSea")
    @Expose
    var elevationFromSea: Int? = null

    @SerializedName("numberOfMosques")
    @Expose
    var numberOfMosques: Int? = null

    @SerializedName("mosqueURL")
    @Expose
    var mosqueURL: String? = null

    @SerializedName("israeliColonies")
    @Expose
    var israeliColonies: Any? = null

    @SerializedName("israeliColoniesArabic")
    @Expose
    var israeliColoniesArabic: String? = null

    @SerializedName("villageNameInHistoryArabic")
    @Expose
    var villageNameInHistoryArabic: String? = null

    @SerializedName("villageNameInHistory")
    @Expose
    var villageNameInHistory: Any? = null

    @SerializedName("neighboringTowns")
    @Expose
    var neighboringTowns: Any? = null

    @SerializedName("neighboringTownsArabic")
    @Expose
    var neighboringTownsArabic: String? = null

    @SerializedName("inhabitantsHistoricalArabic")
    @Expose
    var inhabitantsHistoricalArabic: Any? = null

    @SerializedName("inhabitantsHistorical")
    @Expose
    var inhabitantsHistorical: Any? = null

    @SerializedName("shrines")
    @Expose
    var shrines: Any? = null

    @SerializedName("shrinesArabic")
    @Expose
    var shrinesArabic: String? = null

    @SerializedName("archeologyArabic")
    @Expose
    var archeologyArabic: String? = null

    @SerializedName("numberOfChurchs")
    @Expose
    var numberOfChurchs: Int? = null

    @SerializedName("churchURL")
    @Expose
    var churchURL: String? = null

    @SerializedName("school")
    @Expose
    var school: List<School>? = null

    @SerializedName("hebrewName")
    @Expose
    var hebrewName: String? = null

    @SerializedName("ethnicallyCleansed")
    @Expose
    var ethnicallyCleansed: Boolean? = null

    @SerializedName("keywords")
    @Expose
    var keywords: String? = null

    @SerializedName("occupationDate")
    @Expose
    var occupationDate: String? = null

    @SerializedName("destructionRefNum")
    @Expose
    var destructionRefNum: Int? = null

    @SerializedName("exodusCause")
    @Expose
    var exodusCause: Int? = null

    @SerializedName("numOfHouses")
    @Expose
    var numOfHouses: List<Any>? = null

    @SerializedName("villageLand")
    @Expose
    var villageLand: Int? = null

    @SerializedName("totalLand")
    @Expose
    var totalLand: Int? = null

    @SerializedName("jewishLand")
    @Expose
    var jewishLand: Int? = null

    @SerializedName("usurpedLand")
    @Expose
    var usurpedLand: Int? = null

    @SerializedName("usurpedLand1962")
    @Expose
    var usurpedLand1962: Int? = null

    @SerializedName("insideAL")
    @Expose
    var insideAL: Int? = null

    @SerializedName("withinAL")
    @Expose
    var withinAL: Int? = null

    @SerializedName("population")
    @Expose
    var population: List<Population>? = null

    @SerializedName("cultivableLand")
    @Expose
    var cultivableLand: Int? = null

    @SerializedName("builtUpArea")
    @Expose
    var builtUpArea: Int? = null

    @SerializedName("daysSinceOccupation")
    @Expose
    var daysSinceOccupation: Int? = null

    @SerializedName("arabPopulation1948")
    @Expose
    var arabPopulation1948: Int? = null

    @SerializedName("zochrotNum")
    @Expose
    var zochrotNum: Int? = null

    @SerializedName("nakbaOnline")
    @Expose
    var nakbaOnline: String? = null

    @SerializedName("historicalBackgroundArabic")
    @Expose
    var historicalBackgroundArabic: String? = null

    @SerializedName("atlasNum")
    @Expose
    var atlasNum: Int? = null

    @SerializedName("israeliOperationCD")
    @Expose
    var israeliOperationCD: String? = null

    @SerializedName("defendersCD")
    @Expose
    var defendersCD: Any? = null

    @SerializedName("massacresCD")
    @Expose
    var massacresCD: String? = null

    @SerializedName("publicLand")
    @Expose
    var publicLand: Int? = null

    @SerializedName("oliveArea")
    @Expose
    var oliveArea: Int? = null

    @SerializedName("citrusArea")
    @Expose
    var citrusArea: Int? = null

    @SerializedName("numberOfShrines")
    @Expose
    var numberOfShrines: Int? = null

    @SerializedName("totalEstimatedRefgugees1998")
    @Expose
    var totalEstimatedRefgugees1998: Int? = null

    @SerializedName("arabicLand")
    @Expose
    var arabicLand: Int? = null

    @SerializedName("totalBuiltUpArea")
    @Expose
    var totalBuiltUpArea: Int? = null

    @SerializedName("totalCerealArea")
    @Expose
    var totalCerealArea: Int? = null

    @SerializedName("totalNonCultivableLand")
    @Expose
    var totalNonCultivableLand: Int? = null

    @SerializedName("totalCitrusArea")
    @Expose
    var totalCitrusArea: Int? = null

    @SerializedName("jewishCitrusArea")
    @Expose
    var jewishCitrusArea: Int? = null

    @SerializedName("jewishIrrigateOrchardsArea")
    @Expose
    var jewishIrrigateOrchardsArea: Int? = null

    @SerializedName("jewishCerealArea")
    @Expose
    var jewishCerealArea: Int? = null

    @SerializedName("jewishBuiltUpArea")
    @Expose
    var jewishBuiltUpArea: Int? = null

    @SerializedName("jewishCultivableLand")
    @Expose
    var jewishCultivableLand: Int? = null

    @SerializedName("jewishNonCultivableLand")
    @Expose
    var jewishNonCultivableLand: Int? = null

    @SerializedName("arabCitrusArea")
    @Expose
    var arabCitrusArea: Int? = null

    @SerializedName("totalIrrigateOrchardsArea")
    @Expose
    var totalIrrigateOrchardsArea: Int? = null

    @SerializedName("totalCultivableLand")
    @Expose
    var totalCultivableLand: Int? = null

    @SerializedName("beforeOccupationArabic")
    @Expose
    var beforeOccupationArabic: String? = null

    @SerializedName("occupationArabic")
    @Expose
    var occupationArabic: String? = null

    @SerializedName("townTodayArabic")
    @Expose
    var townTodayArabic: String? = null

    @SerializedName("originalDistrict")
    @Expose
    var originalDistrict: String? = null

    @SerializedName("atlasName")
    @Expose
    var atlasName: String? = null

    @SerializedName("attackingUnitCD")
    @Expose
    var attackingUnitCD: Any? = null

    @SerializedName("shrinesURL")
    @Expose
    var shrinesURL: String? = null

    @SerializedName("includesGP")
    @Expose
    var includesGP: String? = null

    @SerializedName("mainImageURL")
    @Expose
    var mainImageURL: String? = null

    @SerializedName("picturesGeoPoint")
    @Expose
    var picturesGeoPoint: Int? = null

    @SerializedName("familiesURL")
    @Expose
    var familiesURL: String? = null

    @SerializedName("aerialViews")
    @Expose
    var aerialViews: String? = null

    @SerializedName("arabBuiltUpArea")
    @Expose
    var arabBuiltUpArea: Int? = null

    @SerializedName("arabCerealArea")
    @Expose
    var arabCerealArea: Int? = null

    @SerializedName("arabCultivableLand")
    @Expose
    var arabCultivableLand: Int? = null

    @SerializedName("arabIrrigateOrchardsArea")
    @Expose
    var arabIrrigateOrchardsArea: Int? = null

    @SerializedName("arabLand")
    @Expose
    var arabLand: Int? = null

    @SerializedName("arabNonCultivableLand")
    @Expose
    var arabNonCultivableLand: Int? = null

    @SerializedName("arabPopulation1945")
    @Expose
    var arabPopulation1945: Int? = null

    @SerializedName("arijArabicURL")
    @Expose
    var arijArabicURL: String? = null

    @SerializedName("arijURL")
    @Expose
    var arijURL: String? = null

    @SerializedName("beforeAfterArticleId")
    @Expose
    var beforeAfterArticleId: Int? = null

    @SerializedName("biladunaFilisteenPage")
    @Expose
    var biladunaFilisteenPage: String? = null

    @SerializedName("educationVillageGeoPoint")
    @Expose
    var educationVillageGeoPoint: Int? = null

    @SerializedName("featuredArticleURL")
    @Expose
    var featuredArticleURL: String? = null

    @SerializedName("featuredFBPage")
    @Expose
    var featuredFBPage: String? = null

    @SerializedName("featuredURL")
    @Expose
    var featuredURL: String? = null

    @SerializedName("featuredURL2")
    @Expose
    var featuredURL2: String? = null

    @SerializedName("featuredURL3")
    @Expose
    var featuredURL3: String? = null

    @SerializedName("featuredURL4")
    @Expose
    var featuredURL4: String? = null

    @SerializedName("howiyyaURL")
    @Expose
    var howiyyaURL: String? = null

    @SerializedName("howiyyaURLArabic")
    @Expose
    var howiyyaURLArabic: String? = null

    @SerializedName("includedIn")
    @Expose
    var includedIn: Int? = null

    @SerializedName("jewishPopulation1945")
    @Expose
    var jewishPopulation1945: Int? = null

    @SerializedName("nakbaPage")
    @Expose
    var nakbaPage: String? = null

    @SerializedName("numOfPeopleKnewHowToRead")
    @Expose
    var numOfPeopleKnewHowToRead: Int? = null

    @SerializedName("pgr")
    @Expose
    var pgr: Int? = null

    @SerializedName("palOpenMapsUrl")
    @Expose
    var palOpenMapsUrl: String? = null

    @SerializedName("palestineEncyclopediaArticleId")
    @Expose
    var palestineEncyclopediaArticleId: Int? = null

    @SerializedName("palestineEncyclopediaUrl")
    @Expose
    var palestineEncyclopediaUrl: String? = null

    @SerializedName("publicBuiltUpArea")
    @Expose
    var publicBuiltUpArea: Int? = null

    @SerializedName("publicCerealArea")
    @Expose
    var publicCerealArea: Int? = null

    @SerializedName("publicCultivableLand")
    @Expose
    var publicCultivableLand: Int? = null

    @SerializedName("publicNonCultivableLand")
    @Expose
    var publicNonCultivableLand: Int? = null

    @SerializedName("registeredRefgugees1998")
    @Expose
    var registeredRefgugees1998: Int? = null

    @SerializedName("reservedFields")
    @Expose
    var reservedFields: List<Any>? = null

    @SerializedName("reservedFieldsCount")
    @Expose
    var reservedFieldsCount: Int? = null

    @SerializedName("reservedFieldsInt")
    @Expose
    var reservedFieldsInt: List<Any>? = null

    @SerializedName("reservedFieldsIntCount")
    @Expose
    var reservedFieldsIntCount: Int? = null

    @SerializedName("rewaqURL")
    @Expose
    var rewaqURL: String? = null

    @SerializedName("townMapURL")
    @Expose
    var townMapURL: String? = null

    @SerializedName("usurpedLandIn1953")
    @Expose
    var usurpedLandIn1953: Int? = null

    @SerializedName("villageCouncil")
    @Expose
    var villageCouncil: Boolean? = null

    @SerializedName("wikiURL")
    @Expose
    var wikiURL: String? = null

    @SerializedName("wikiURLArabic")
    @Expose
    var wikiURLArabic: String? = null

    @SerializedName("name")
    @Expose
    var name: Any? = null
}

class LandOwnership {
    @SerializedName("total")
    @Expose
    var total: String? = null

    @SerializedName("cultivable")
    @Expose
    var cultivable: String? = null

    @SerializedName("builtup")
    @Expose
    var builtup: String? = null

    @SerializedName("arab")
    @Expose
    var arab: String? = null

    @SerializedName("jewish")
    @Expose
    var jewish: String? = null

    @SerializedName("per_total")
    @Expose
    var perTotal: String? = null

    @SerializedName("public")
    @Expose
    var public: String? = null
}

class Population {
    @SerializedName("year")
    @Expose
    var year: Int? = null

    @SerializedName("type")
    @Expose
    var type: Any? = null

    @SerializedName("count")
    @Expose
    var count: String? = null
}

class School {
    @SerializedName("type")
    @Expose
    var type: Int? = null

    @SerializedName("highestGrade")
    @Expose
    var highestGrade: Int? = null

    @SerializedName("builtYear")
    @Expose
    var builtYear: Int? = null

    @SerializedName("numberOfStudents")
    @Expose
    var numberOfStudents: Int? = null

    @SerializedName("censusYear")
    @Expose
    var censusYear: Int? = null

    @SerializedName("numberOfTeachers")
    @Expose
    var numberOfTeachers: Int? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("order")
    @Expose
    var order: Int? = null

    @SerializedName("numberOfBooks")
    @Expose
    var numberOfBooks: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null
}

class Town {
    @SerializedName("ethnic_cleansing_6")
    @Expose
    var ethnicCleansing6: String? = null

    @SerializedName("name_in_arabic")
    @Expose
    var nameInArabic: String? = null

    @SerializedName("palgatesURL")
    @Expose
    var palgatesURL: String? = null

    @SerializedName("settlements_7")
    @Expose
    var settlements7: String? = null

    @SerializedName("town_before_5")
    @Expose
    var townBefore5: String? = null

    @SerializedName("town_today_8")
    @Expose
    var townToday8: String? = null

    @SerializedName("total")
    @Expose
    var total: Any? = null

    @SerializedName("village_statistics")
    @Expose
    var villageStatistics: VillageStatistics? = null

    @SerializedName("nakbaInHebrewId")
    @Expose
    var nakbaInHebrewId: String? = null

    @SerializedName("nakbaOnlineFile")
    @Expose
    var nakbaOnlineFile: String? = null
}

class VillageStatistics {
    @SerializedName("archeological_sites")
    @Expose
    var archeologicalSites: String? = null

    @SerializedName("brigade")
    @Expose
    var brigade: String? = null

    @SerializedName("clane_names")
    @Expose
    var claneNames: String? = null

    @SerializedName("acts_of_terror")
    @Expose
    var actsOfTerror: String? = null

    @SerializedName("alternate_map")
    @Expose
    var alternateMap: String? = null

    @SerializedName("free_text")
    @Expose
    var freeText: String? = null

    @SerializedName("nearby_wadies")
    @Expose
    var nearbyWadies: String? = null

    @SerializedName("neighboring_villages")
    @Expose
    var neighboringVillages: String? = null

    @SerializedName("cutivatable1596")
    @Expose
    var cutivatable1596: String? = null

    @SerializedName("destroyed")
    @Expose
    var destroyed: String? = null

    @SerializedName("famous_villagers")
    @Expose
    var famousVillagers: String? = null

    @SerializedName("shrines")
    @Expose
    var shrines: String? = null

    @SerializedName("settlement_names")
    @Expose
    var settlementNames: String? = null

    @SerializedName("militery_operation")
    @Expose
    var militeryOperation: String? = null

    @SerializedName("refugee_camps")
    @Expose
    var refugeeCamps: String? = null

    @SerializedName("refuges_migration_route")
    @Expose
    var refugesMigrationRoute: String? = null

    @SerializedName("religious_institutions")
    @Expose
    var religiousInstitutions: String? = null

    @SerializedName("nick_name")
    @Expose
    var nickName: String? = null

    @SerializedName("no_of_houses")
    @Expose
    var noOfHouses: String? = null

    @SerializedName("population1947")
    @Expose
    var population1947: Any? = null

    @SerializedName("residence_origination")
    @Expose
    var residenceOrigination: String? = null

    @SerializedName("schools")
    @Expose
    var schools: String? = null

    @SerializedName("sections")
    @Expose
    var sections: String? = null

    @SerializedName("village_council")
    @Expose
    var villageCouncil: String? = null

    @SerializedName("village_defendors")
    @Expose
    var villageDefendors: String? = null

    @SerializedName("water_sources")
    @Expose
    var waterSources: String? = null

    @SerializedName("village_other_names")
    @Expose
    var villageOtherNames: String? = null

    @SerializedName("days_since_occupation")
    @Expose
    var daysSinceOccupation: String? = null

    @SerializedName("elevation_from_sea")
    @Expose
    var elevationFromSea: String? = null

    @SerializedName("ethnically_cleansed")
    @Expose
    var ethnicallyCleansed: String? = null

    @SerializedName("population1999")
    @Expose
    var population1999: String? = null

    @SerializedName("distance_from_district")
    @Expose
    var distanceFromDistrict: String? = null

    @SerializedName("occupation_date")
    @Expose
    var occupationDate: String? = null

    @SerializedName("population1596")
    @Expose
    var population1596: String? = null

    @SerializedName("population1800")
    @Expose
    var population1800: String? = null

    @SerializedName("population1922")
    @Expose
    var population1922: String? = null

    @SerializedName("population1931")
    @Expose
    var population1931: String? = null

    @SerializedName("population1944")
    @Expose
    var population1944: String? = null

    @SerializedName("land_ownership")
    @Expose
    var landOwnership: LandOwnership? = null

    @SerializedName("population1961")
    @Expose
    var population1961: String? = null
}


class Comment {
    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("notifiedAuthor")
    @Expose
    var notifiedAuthor: Boolean? = null

    @SerializedName("activityId")
    @Expose
    var activityId: Int? = null

    @SerializedName("contactId")
    @Expose
    var contactId: Int? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("createDate")
    @Expose
    var createDate: String? = null

    @SerializedName("commentId")
    @Expose
    var commentId: Int? = null

    @SerializedName("language")
    @Expose
    var language: Int? = null

    @SerializedName("content")
    @Expose
    var content: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("value")
    @Expose
    var value: String? = null
}



 class Comments {
    @SerializedName("comment")
    @Expose
    var comment: List<Comment>? = null

    @SerializedName("prevCommentCount")
    @Expose
    var prevCommentCount: Int? = null

    @SerializedName("nextLink")
    @Expose
    var nextLink: String? = null

    @SerializedName("prevLink")
    @Expose
    var prevLink: String? = null
}


class TownPicture {
    @SerializedName("pictureId")
    @Expose
    var pictureId: Int? = null

    @SerializedName("townId")
    @Expose
    var townId: Int? = null

    @SerializedName("geoPointId")
    @Expose
    var geoPointId: Int? = null

    @SerializedName("subjectId")
    @Expose
    var subjectId: Int? = null

    @SerializedName("pictureType")
    @Expose
    var pictureType: Int? = null

    @SerializedName("order")
    @Expose
    var order: Int? = null

    @SerializedName("contactId")
    @Expose
    var contactId: Int? = null

    @SerializedName("height")
    @Expose
    var height: Int? = null

    @SerializedName("width")
    @Expose
    var width: Int? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("showImage")
    @Expose
    var showImage: Boolean? = null

    @SerializedName("noComments")
    @Expose
    var noComments: Boolean? = null

    @SerializedName("commentSortAsc")
    @Expose
    var commentSortAsc: Boolean? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("arabicTitle")
    @Expose
    var arabicTitle: String? = null

    @SerializedName("imageName")
    @Expose
    var imageName: String? = null

    @SerializedName("fileName")
    @Expose
    var fileName: String? = null

    @SerializedName("htmlFileName")
    @Expose
    var htmlFileName: String? = null

    @SerializedName("size")
    @Expose
    var size: Int? = null

    @SerializedName("createDate")
    @Expose
    var createDate: String? = null

    @SerializedName("updateDate")
    @Expose
    var updateDate: String? = null

    @SerializedName("comments")
    @Expose
    var comments: Comments? = null

    @SerializedName("htmlLink")
    @Expose
    var htmlLink: String? = null
}