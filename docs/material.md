# Texture Sets
Material texture sets are collections of one or more texture types that represent a look-and-feel for a material. Examples are dull, shiny, metallic or wood.  

Texture sets are defined using JSON-models in the `<namspace>:models/material` directory.  
This means that a texture set named `coolmod:daset` should have a json file located at 
`coolmod:models/material/daset.json`.

## Json Structure
```json5
{
	// The parent set to use when looking for a texture that doesn't exist.
	// If omitted, conductance:dull will be used
	"parent": "conductance:shiny",
	// Optional texture to render on top of all other textures in the set.
	// Does not cascade like other textures in a set
	"overlay":"conductance:material/magnetic_overlay"
}
```