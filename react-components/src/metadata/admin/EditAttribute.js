/**
 * @author psarando
 */
import React, { Component, Fragment } from "react";
import { Field, FieldArray, FormSection } from "redux-form";
import { injectIntl } from "react-intl";

import {
    FormCheckbox,
    FormSelectField,
    FormTextField,
} from "../../util/FormField";
import withI18N, { getMessage, formatMessage } from "../../util/I18NWrapper";

import intlData from "../messages";
import styles from "../style";
import AttributeEnumEditGrid from "./AttributeEnumEditGrid";
import OntologyLookupServiceSettings from "./OntologyLookupServiceSettings";
import SlideUpTransition from "./SlideUpTransition";
import TemplateAttributeList from "./TemplateAttributeList";

import AppBar from "@material-ui/core/AppBar";
import Dialog from "@material-ui/core/Dialog";
import DialogContent from "@material-ui/core/DialogContent";
import Divider from "@material-ui/core/Divider";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import IconButton from "@material-ui/core/IconButton";
import MenuItem from "@material-ui/core/MenuItem";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import ArrowBack from "@material-ui/icons/ArrowBack";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";

const AttributeTypes = [
    "String",
    "Timestamp",
    "Boolean",
    "Number",
    "Integer",
    "Multiline Text",
    "URL/URI",
    "Enum",
    "OLS Ontology Term",
    "UAT Ontology Term",
];

const AttributeTypeMenuItems = AttributeTypes.map((type, index) => (<MenuItem key={index} value={type}>{type}</MenuItem>));

class EditAttribute extends Component {
    constructor(props) {
        super(props);

        this.state = {
            editingAttrIndex: -1,
        };
    }

    normalizeType = (type) => {
        const { change, field, attribute: { settings } } = this.props;

        if (type === "OLS Ontology Term" && (!settings || !settings.type)) {
            change(`${field}.settings`, {...settings, type: "CLASS"});
        }

        return type;
    };

    render() {
        const { classes, intl, change, field, attribute, open, parentName } = this.props;
        const { name, type, attributes } = attribute;
        const { editingAttrIndex } = this.state;

        return (
            <Dialog
                open={open}
                onClose={this.props.closeAttrDialog}
                fullScreen
                disableBackdropClick
                disableEscapeKeyDown
                aria-labelledby="form-dialog-title"
                TransitionComponent={SlideUpTransition}
            >
                <AppBar className={classes.appBar}>
                    <Toolbar>
                        <IconButton color="inherit" onClick={this.props.closeAttrDialog} aria-label={formatMessage(intl, "back")}>
                            <ArrowBack />
                        </IconButton>
                        <Typography variant="title" color="inherit" className={classes.flex}>
                            {getMessage("dialogTitleEditAttributeFor", {values: { parentName }})}
                        </Typography>
                    </Toolbar>
                </AppBar>
                <DialogContent>

                    <Field name={`${field}.name`}
                           label={getMessage("attrNameLabel")}
                           id="attrName"
                           required={true}
                           autoFocus
                           margin="dense"
                           component={FormTextField}
                    />
                    <Field name={`${field}.description`}
                           label={getMessage("description")}
                           id="attrDescription"
                           component={FormTextField}
                    />

                    <Field name={`${field}.type`}
                           label={getMessage("attrTypeLabel")}
                           id="attrType"
                           component={FormSelectField}
                           normalize={this.normalizeType}
                    >
                        {AttributeTypeMenuItems}
                    </Field>

                    <Field name={`${field}.required`}
                           label={getMessage("attrRequiredLabel")}
                           id="attrRequired"
                           color="primary"
                           component={FormCheckbox}
                    />

                    <Divider />

                    {type === "Enum" &&
                    <ExpansionPanel defaultExpanded>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography className={classes.heading}>{getMessage("enumValues")}</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <FieldArray name={`${field}.values`}
                                        component={AttributeEnumEditGrid}
                                        change={change}
                            />
                        </ExpansionPanelDetails>
                    </ExpansionPanel>
                    }

                    {type === "OLS Ontology Term" &&
                    <ExpansionPanel defaultExpanded>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography className={classes.heading}>{getMessage("olsQueryParams")}</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <FormSection name={`${field}.settings`}
                                         component={OntologyLookupServiceSettings}
                            />
                        </ExpansionPanelDetails>
                    </ExpansionPanel>
                    }

                    <ExpansionPanel defaultExpanded={attributes && attributes.length > 0}>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography className={classes.heading}>{getMessage("attributes")}</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <FieldArray name={`${field}.attributes`}
                                        component={TemplateAttributeList}
                                        onEditAttr={(index) => this.setState({editingAttrIndex: index})}
                            />
                        </ExpansionPanelDetails>
                    </ExpansionPanel>


                    <FieldArray name={`${field}.attributes`}
                                component={FormDialogEditAttribute}
                                editingAttrIndex={editingAttrIndex}
                                parentName={name}
                                closeAttrDialog={() => this.setState({editingAttrIndex: -1})}
                    />
                </DialogContent>
            </Dialog>
        );
    }
}

EditAttribute = withStyles(styles)(withI18N(injectIntl(EditAttribute), intlData));

const FormDialogEditAttribute = ({ fields, change, editingAttrIndex, parentName, closeAttrDialog }) => (
    <Fragment>
        {fields.map((field, index) => (
            <EditAttribute key={field}
                           field={field}
                           change={change}
                           attribute={fields.get(index)}
                           open={editingAttrIndex === index}
                           parentName={parentName}
                           closeAttrDialog={closeAttrDialog}
            />
        ))
        }
    </Fragment>
);

export default FormDialogEditAttribute;
