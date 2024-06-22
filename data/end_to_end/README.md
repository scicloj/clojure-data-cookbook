# Example data for End to End chapter example

The datasets in this folder were downloaded on June 12, 2024 from "the datazONE", operated by the Nova Scotia Property Valuation Services Corporation.

These datasets are licensed under the _Open Data & Information Government Licence – PVSC & Participating Municipalities_. Details can be found in the [LICENSE](./LICENSE) file.

Metadata for each dataset at the time of retrieval is listed below.

## Parcel Sales History

Provides the address, GPS coordinates, sales date and sales price for all sales in Nova Scotia from July 2010 onwards.

### About this Dataset

| Field | Value |
|-------|-------|
| Updated | June 3, 2024 |
| Data Last Updated | June 3, 2024 |
| Metadata Last Updated | January 15, 2019 |
| Date Created | October 7, 2016 |
| Views | 7,533 |
| Downloads | 2,613 |
| Data Provided by | Property Valuation Services Corporation |
| Dataset Owner | datazONE |
| License Name | Open Data & Information Government Licence – PVSC & Participating Municipalities |
| License URL | https://www.pvsc.ca/en/home/datazone/datazone-license.aspx |
| Data Provider | Property Valuation Services Corporation, Service Centre |
| Category | Assessment |
| Tags | location, coordinates, assessment, taxable assessment, value, aan |
| Rows | 553K |
| Columns | 14 |

### Columns

| Column Name | Description | API Field Name | Data Type |
|-------------|-------------|----------------|-----------|
| Municipal Unit | Nova Scotia municipality | municipal_unit | Text |
| Assessment Account Number | Unique number assigned to each assessed property | aan | Text |
| Civic Number | Address number | address_num | Number |
| Civic Additional | Additional address information such as unit or suite number | address_additional | Text |
| Civic Direction | Street direction e.g. East - E | address_direction | Text |
| Civic Street Name | | address_street | Text |
| Civic Suffix | | address_suffix | Text |
| Civic City Name | City name | address_city | Text |
| Sale Price | | sale_price | Number |
| Sale Date | | sale_date | Floating Timestamp |
| Parcels In Sale | Number of parcels in sale of property | parcels_in_sale | Number |
| Y Map Coordinate | Latitude | y_coord | Number |
| X Map Coordinate | Longitude | x_coord | Number |
| Map Coordinates | Concatenated latitude and longitude for displaying on a map | location | Location |

## Residential Dwelling Characteristics

This dataset contains location, address and property characteristics for residential improved properties in Nova Scotia.

### About this Dataset

| Field | Value |
|-------|-------|
| Updated | January 8, 2024 |
| Data Last Updated | January 8, 2024 |
| Metadata Last Updated | January 15, 2018 |
| Date Created | October 7, 2016 |
| Views | 6,102 |
| Downloads | 1,355 |
| Data Provided by | Property Valuation Services Corporation |
| Dataset Owner | datazONE |
| License Name | Open Data & Information Government Licence – PVSC & Participating Municipalities |
| License URL | https://www.pvsc.ca/en/home/datazone/datazone-license.aspx |
| Data Provider | Property Valuation Services Corporation, Business and Innovation Services |
| Category | Assessment |
| Tags | residential, housing, property, location, address, bedrooms, bathrooms, aan |
| Rows | 379K |
| Columns | 21 |

### Columns

| Column Name | Description | API Field Name | Data Type |
|-------------|-------------|----------------|-----------|
| Municipal Unit | Nova Scotia municipality | municipal_unit | Text |
| Assessment Account Number | Unique number assigned to each assessed property. | aan | Text |
| Civic Number | Address number | address_num | Number |
| Civic Additional | Additional address information such as unit or suite number | address_additional | Text |
| Civic Direction | Street direction e.g. East - E | address_direction | Text |
| Civic Street Name | | address_street | Text |
| Civic Street Suffix | | address_suffix | Text |
| Civic City Name | City name | address_city | Text |
| Living Units | Total number of living units on property | living_units | Number |
| Year Built | Year of construction | year_built | Number |
| Square Foot Living Area | Living area of property in square feet | square_foot_living_area | Number |
| Style | | style | Text |
| Bedrooms | | bedrooms | Number |
| Bathrooms | | bathrooms | Number |
| Under Construction | | under_construction | Text |
| Construction Grade | | grade | Text |
| Finished Basement | | finished_basement | Text |
| Garage | | garage | Text |
| Y Map Coordinate | Latitude | y_coord | Number |
| X Map Coordinate | Longitude | x_coord | Number |
| Map Coordinates | Concatenated latitude and longitude for displaying on a map | location | Location |

## Parcel Land Sizes

This dataset contains all assessment account numbers and associated land sizes and civic addresses with geographic coordinates.

Note: Only one civic address is displayed per property. There are properties with multiple civic addresses.

### About this Dataset

| Field | Value |
| --- | --- |
| Updated | January 8, 2024 |
| Data Last Updated | January 8, 2024 |
| Metadata Last Updated | January 15, 2019 |
| Date Created | October 5, 2016 |
| Views | 3,365 |
| Downloads | 1,255 |
| Data Provided by | Property Valuation Services Corporation |
| Dataset Owner | datazONE |
| License Name | Open Data & Information Government Licence – PVSC & Participating Municipalities |
| License URL | https://www.pvsc.ca/en/home/datazone/datazone-license.aspx |
| Data Provider | Property Valuation Services Corporation, Business and Innovation Services |
| Category | Assessment |
| Tags | acres, aan, land size, address |
| Rows | 647K |
| Columns | 13 |

### Columns

| Column Name | Description | API Field Name | Data Type |
|-------------|-------------|----------------|-----------|
| Municipal Unit | Nova Scotia Municipality | municipal_unit | Text |
| Assessment Account Number | Unique number assigned to each assessed property | aan | Text |
| Civic Number | | address_num | Number |
| Civic Additional | Additional address information such as a unit or suite number | address_add | Text |
| Civic Direction | Street direction e.g. East - E | address_direction | Text |
| Civic Street Name | | address_street | Text |
| Civic Street Suffix | | address_suffix | Text |
| Civic City Name | | address_city | Text |
| Acreage | | land_acres | Number |
| Square Feet | | land_square_feet | Number |
| Y Map Coordinate | Latitude | y_coord | Number |
| X Map Coordinate | Longitude | x_coord | Number |
| Map Coordinates | Concatenated latitude and longitude for displaying on a map | location | Location |
